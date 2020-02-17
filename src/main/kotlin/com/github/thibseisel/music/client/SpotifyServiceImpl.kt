package com.github.thibseisel.music.client

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.thibseisel.music.spotify.*
import com.github.thibseisel.music.spotify.SpotifyAudioAnalysis
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import org.springframework.web.reactive.function.client.awaitBody

@Component
internal class SpotifyServiceImpl(
    private val http: WebClient,
    private val mapper: ObjectMapper
) : SpotifyService {

    override suspend fun search(query: String, offset: Int, limit: Int): List<SpotifyTrack> {
        try {
            val searchResults = http.get()
                .uri {
                    it.path("/search")
                    it.queryParam("type", "track")
                    it.queryParam("q", query)
                    it.queryParam("offset", offset.toString())
                    it.queryParam("limit", limit.toString())
                    it.build()
                }
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .awaitBody<SpotifySearchResult>()

            return searchResults.tracks.items

        } catch (genericFailure: WebClientResponseException) {
            handleSpotifyFailure(genericFailure)
        }
    }

    override suspend fun findArtist(id: String): FullSpotifyArtist? =
        findEntity("/artists/{id}", id)

    override suspend fun findAlbum(id: String): FullSpotifyAlbum? =
        findEntity("/albums/{id}", id)

    override suspend fun findTrack(id: String): FullSpotifyTrack? =
        findEntity("/tracks/{id}", id)

    override suspend fun findAudioFeature(trackId: String): SpotifyAudioFeature? =
        findEntity("/audio-features/{id}", trackId)

    private suspend inline fun <reified T : SpotifyEntity> findEntity(endpoint: String, id: String): T? {
        try {
            return http.get()
                .uri(endpoint, id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .awaitBody()

        } catch (entityNotFound: WebClientResponseException.NotFound) {
            return null
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
        try {
            return http.get()
                .uri("/audio-analysis/{id}", trackId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .awaitBody()

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
}