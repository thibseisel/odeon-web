package com.github.thibseisel.music.client

import com.github.thibseisel.music.spotify.SpotifyAudioFeature
import com.github.thibseisel.music.spotify.SpotifyTrack
import kotlinx.coroutines.flow.Flow
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import org.springframework.web.reactive.function.client.awaitBody

@Component
internal class SpotifyServiceImpl(
    private val http: WebClient
) : SpotifyService {

    override fun search(query: String): Flow<SpotifyTrack> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
            TODO("Handle error")
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
            TODO("Handle error")
        }
    }

    override suspend fun getSeveralAudioFeatures(trackIds: List<String>): List<SpotifyAudioFeature?> {
        try {
            return http.get()
                .uri {
                    it.path("/audio-features")
                    it.queryParam("ids", trackIds.joinToString(","))
                    it.build()
                }
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .awaitBody()
        } catch (genericFailure: WebClientResponseException) {
            TODO("Handle error")
        }
    }
}