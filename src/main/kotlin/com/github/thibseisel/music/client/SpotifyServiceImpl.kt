package com.github.thibseisel.music.client

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.thibseisel.music.spotify.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.reactor.mono
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.*
import reactor.core.publisher.Mono

@Component
internal class SpotifyServiceImpl(
    http: WebClient,
    private val mapper: ObjectMapper
) : SpotifyService {

    private val http: WebClient = http.mutate()
        .filter(RetryAutomaticallyAfterTooManyRequests())
        .build()

    override suspend fun search(query: String, offset: Int, limit: Int): List<SpotifyTrack> {
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

    override suspend fun getSeveralTracks(ids: List<String>): List<FullSpotifyTrack?> {
        try {
            val wrapper = http.get()
                .uri {
                    it.path("/tracks")
                    it.queryParam("ids", ids.joinToString(","))
                    it.build()
                }
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .awaitBody<JsonWrapper<FullSpotifyTrack?>>()
            return wrapper.data

        } catch (genericFailure: WebClientResponseException) {
            handleSpotifyFailure(genericFailure)
        }
    }

    override suspend fun getSeveralAudioFeatures(trackIds: List<String>): List<SpotifyAudioFeature?> {
        try {
            val wrapper = http.get()
                .uri {
                    it.path("/audio-features")
                    it.queryParam("ids", trackIds.joinToString(","))
                    it.build()
                }
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .awaitBody<JsonWrapper<SpotifyAudioFeature?>>()
            return wrapper.data

        } catch (genericFailure: WebClientResponseException) {
            handleSpotifyFailure(genericFailure)
        }
    }

    override suspend fun findAudioAnalysis(trackId: String): SpotifyAudioAnalysis? {
        return findEntity("/audio-analysis/{id}", trackId, SpotifyAudioAnalysis::class.java)
    }

    override suspend fun searchPlaylists(name: String, offset: Int, limit: Int): List<SpotifyPlaylist> {
        return search("playlist", name, offset, limit).playlists?.items
            ?: error("\"playlists\" property of SpotifySearchResult should be present when searching playlists.")
    }

    private suspend fun search(entityType: String, query: String, offset: Int, limit: Int): SpotifySearchResult {
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

    private suspend fun <T : Any> findEntity(endpoint: String, id: String, entityType: Class<T>): T? {
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

    private fun handleSpotifyFailure(httpException: WebClientResponseException): Nothing {
        val errorPayload = mapper.readValue(httpException.responseBodyAsString, SpotifyError::class.java)
        throw SpotifyService.ApiException(httpException.statusCode, errorPayload.message)
    }

    /**
     * Filters Spotify API responses to retry requests automatically after some delay
     * when receiving an HTTP 429 (Too Many Requests).
     */
    private class RetryAutomaticallyAfterTooManyRequests : ExchangeFilterFunction {
        override fun filter(request: ClientRequest, next: ExchangeFunction): Mono<ClientResponse> = mono {
            var response: ClientResponse? = null

            do {
                try {
                    response = next.exchange(request).awaitSingle()
                } catch (tooManyRequests: WebClientResponseException.TooManyRequests) {
                    val retryAfterSeconds = tooManyRequests.headers.getFirst(HttpHeaders.RETRY_AFTER)
                        ?.toLongOrNull()
                        ?: throw tooManyRequests

                    delay(retryAfterSeconds + 1L)
                }
            } while (response == null)
            return@mono response
        }
    }
}
