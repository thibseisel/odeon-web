package com.github.thibseisel.music.client

import com.github.thibseisel.music.spotify.FullSpotifyTrack
import com.github.thibseisel.music.spotify.SpotifyAudioFeature
import com.github.thibseisel.music.spotify.SpotifyTrack
import org.springframework.http.HttpStatus

interface SpotifyService {
    suspend fun search(query: String, offset: Int, limit: Int): List<SpotifyTrack>
    suspend fun findTrack(id: String): FullSpotifyTrack?
    suspend fun findAudioFeature(trackId: String): SpotifyAudioFeature?
    suspend fun getSeveralAudioFeatures(trackIds: List<String>): List<SpotifyAudioFeature?>

    class ApiException(
        val status: HttpStatus,
        val reason: String?
    ) : Exception() {

        override val message: String?
            get() = "[${status.value()}] $reason"
    }
}