package com.github.thibseisel.music.client

import com.github.thibseisel.music.spotify.SpotifyAudioFeature
import com.github.thibseisel.music.spotify.SpotifyTrack
import kotlinx.coroutines.flow.Flow

interface SpotifyService {
    fun search(query: String): Flow<SpotifyTrack>
    suspend fun findTrack(id: String): SpotifyTrack?
    suspend fun findAudioFeature(trackId: String): SpotifyAudioFeature?
    suspend fun getSeveralAudioFeatures(trackIds: List<String>): List<SpotifyAudioFeature?>
}