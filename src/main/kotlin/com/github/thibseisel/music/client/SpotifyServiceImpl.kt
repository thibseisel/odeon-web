package com.github.thibseisel.music.client

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.thibseisel.music.spotify.*
import org.springframework.http.HttpStatus
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

    override suspend fun findTrack(id: String): SpotifyTrack? {
        try {
            return http.get()
                .uri("/tracks/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .awaitBody()

        } catch (trackNotFound: WebClientResponseException.NotFound) {
            return null
        } catch (genericFailure: WebClientResponseException) {
            handleSpotifyFailure(genericFailure)
        }
    }

    override suspend fun findAudioFeature(trackId: String): SpotifyAudioFeature? {
        try {
            return http.get()
                .uri("/audio-features/{id}", trackId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .awaitBody()
        } catch (featureNotFound: WebClientResponseException.NotFound) {
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

    private fun handleSpotifyFailure(httpException: WebClientResponseException): Nothing {
        val errorPayload = mapper.readValue(httpException.responseBodyAsString, SpotifyError::class.java)
        throw SpotifyService.ApiException(httpException.statusCode, errorPayload.message)
    }
}