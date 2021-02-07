package com.github.thibseisel.music.client

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.thibseisel.music.spotify.*
import kotlinx.coroutines.*
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.reactor.mono
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.*
import reactor.core.publisher.Mono

private const val MAX_SEVERAL_TRACKS = 50
private const val MAX_SEVERAL_FEATURES = 100

@Component
internal class SpotifyServiceImpl(
    http: WebClient,
    private val mapper: ObjectMapper
) : SpotifyService {

    private val http: WebClient = http.mutate()
        .filter(RetryAutomaticallyAfterTooManyRequests())
        .build()

    override suspend fun searchTracks(query: String, offset: Int, limit: Int): List<SpotifyTrack> {
        return search("track", query, offset, limit).tracks?.items
            ?: error("\"tracks\" property of SpotifySearchResult should be present when searching tracks.")
    }

    override suspend fun findArtist(id: String): FullSpotifyArtist? =
        findEntity("/artists/{id}", id, FullSpotifyArtist::class.java)

    override suspend fun findAlbum(id: String): FullSpotifyAlbum? =
        findEntity("/albums/{id}", id, FullSpotifyAlbum::class.java)

    override suspend fun findTrack(id: String): FullSpotifyTrack? =
        findEntity("/tracks/{id}", id, FullSpotifyTrack::class.java)

    override suspend fun findAudioFeature(trackId: String): SpotifyAudioFeature? =
        findEntity("/audio-features/{id}", trackId, SpotifyAudioFeature::class.java)

    override suspend fun getSeveralTracks(ids: List<String>): List<FullSpotifyTrack?> =
        coroutineScope {
            ids.chunked(MAX_SEVERAL_TRACKS) { getSeveralTracksAsync(it) }
                .awaitAll()
                .flatten()
        }

    override suspend fun getSeveralAudioFeatures(trackIds: List<String>): List<SpotifyAudioFeature?> =
        coroutineScope {
            trackIds.chunked(MAX_SEVERAL_FEATURES) { getSeveralAudioFeaturesAsync(it) }
                .awaitAll()
                .flatten()
        }

    override suspend fun findAudioAnalysis(trackId: String): SpotifyAudioAnalysis? {
        return findEntity("/audio-analysis/{id}", trackId, SpotifyAudioAnalysis::class.java)
    }

    override suspend fun searchPlaylists(
        name: String,
        offset: Int,
        limit: Int
    ): List<SpotifyPlaylist> {
        return search("playlist", name, offset, limit).playlists?.items
            ?: error("\"playlists\" property of SpotifySearchResult should be present when searching playlists.")
    }

    private suspend fun search(
        entityType: String,
        query: String,
        offset: Int,
        limit: Int
    ): SpotifySearchResult {
        try {
            return http.get()
                .uri {
                    it.path("/search")
                    it.queryParam("type", entityType)
                    it.queryParam("q", query)
                    it.queryParam("offset", offset.toString())
                    it.queryParam("limit", limit.toString())
                    it.build()
                }
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .awaitBody()

        } catch (genericFailure: WebClientResponseException) {
            handleSpotifyFailure(genericFailure)
        }
    }

    override suspend fun findPlaylist(id: String): SpotifyPlaylist? {
        return findEntity("/playlists/{id}", id, SpotifyPlaylist::class.java)
    }

    override suspend fun getPlaylistTracks(playlistId: String): List<SpotifyTrack>? {
        try {
            val tracks = mutableListOf<SpotifyTrack>()
            var hasNextPage: Boolean
            var offset = 0

            do {
                val pageOfTracks = http.get()
                    .uri {
                        it.path("/playlists/{id}/tracks")
                        it.queryParam("offset", offset)
                        it.build(playlistId)
                    }
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .awaitBody<Paging<SpotifyPlaylistTrack>>()

                pageOfTracks.items.mapTo(tracks, SpotifyPlaylistTrack::track)
                hasNextPage = pageOfTracks.next != null
                offset = pageOfTracks.offset + pageOfTracks.limit

            } while (hasNextPage)

            return tracks

        } catch (playlistNotFound: WebClientResponseException.NotFound) {
            return null
        } catch (genericFailure: WebClientResponseException) {
            handleSpotifyFailure(genericFailure)
        }
    }

    private suspend fun <T : Any> findEntity(
        endpoint: String,
        id: String,
        entityType: Class<T>
    ): T? {
        try {
            return http.get()
                .uri(endpoint, id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(entityType)
                .awaitSingle()
        } catch (entityNotFound: WebClientResponseException.NotFound) {
            return null
        } catch (genericFailure: WebClientResponseException) {
            handleSpotifyFailure(genericFailure)
        }
    }

    private suspend fun <T : Any> findSeveralEntities(
        endpoint: String,
        ids: List<String>,
        typeToken: ParameterizedTypeReference<JsonWrapper<T>>
    ): List<T?> {
        try {
            val wrapper = http.get()
                .uri {
                    it.path(endpoint)
                    it.queryParam("ids", ids.joinToString(","))
                    it.build()
                }
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(typeToken)
                .awaitSingle()
            return wrapper.data

        } catch (genericFailure: WebClientResponseException) {
            handleSpotifyFailure(genericFailure)
        }
    }

    private fun CoroutineScope.getSeveralTracksAsync(ids: List<String>) = async {
        require(ids.size <= MAX_SEVERAL_TRACKS) {
            "At most $MAX_SEVERAL_TRACKS tracks could be queried at once"
        }

        val trackTypeToken = object : ParameterizedTypeReference<JsonWrapper<FullSpotifyTrack>>() {}
        findSeveralEntities("/tracks", ids, trackTypeToken)
    }

    private fun CoroutineScope.getSeveralAudioFeaturesAsync(trackIds: List<String>) = async {
        require(trackIds.size <= MAX_SEVERAL_FEATURES) {
            "At most $MAX_SEVERAL_FEATURES audio features could be queried at once"
        }

        val featureTypeToken =
            object : ParameterizedTypeReference<JsonWrapper<SpotifyAudioFeature>>() {}
        findSeveralEntities("/audio-features", trackIds, featureTypeToken)
    }

    private fun handleSpotifyFailure(httpException: WebClientResponseException): Nothing {
        val errorPayload =
            mapper.readValue(httpException.responseBodyAsString, SpotifyError::class.java)
        throw SpotifyService.ApiException(httpException.statusCode, errorPayload.message)
    }

    /**
     * Filters Spotify API responses to retry requests automatically after some delay
     * when receiving an HTTP 429 (Too Many Requests).
     */
    private class RetryAutomaticallyAfterTooManyRequests : ExchangeFilterFunction {
        override fun filter(request: ClientRequest, next: ExchangeFunction): Mono<ClientResponse> =
            mono {
                var response: ClientResponse? = null

                do {
                    try {
                        response = next.exchange(request).awaitSingle()
                    } catch (tooManyRequests: WebClientResponseException.TooManyRequests) {
                        val retryAfterSeconds =
                            tooManyRequests.headers.getFirst(HttpHeaders.RETRY_AFTER)
                                ?.toLongOrNull()
                                ?: throw tooManyRequests

                        delay((retryAfterSeconds + 1L) * 1000L)
                    }
                } while (response == null)
                return@mono response
            }
    }
}
