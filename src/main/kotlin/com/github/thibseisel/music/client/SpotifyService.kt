package com.github.thibseisel.music.client

import com.github.thibseisel.music.spotify.*
import com.github.thibseisel.music.spotify.SpotifyAudioAnalysis
import org.springframework.http.HttpStatus

/**
 * Main entry point for retrieving music metadata from the Spotify API.
 */
internal interface SpotifyService {
    suspend fun search(query: String, offset: Int, limit: Int): List<SpotifyTrack>
    suspend fun findArtist(id: String): FullSpotifyArtist?
    suspend fun findAlbum(id: String): FullSpotifyAlbum?
    suspend fun findTrack(id: String): FullSpotifyTrack?
    suspend fun getSeveralTracks(ids: List<String>): List<FullSpotifyTrack?>
    suspend fun findAudioFeature(trackId: String): SpotifyAudioFeature?
    suspend fun getSeveralAudioFeatures(trackIds: List<String>): List<SpotifyAudioFeature?>
    suspend fun findAudioAnalysis(trackId: String): SpotifyAudioAnalysis?

    suspend fun searchPlaylists(name: String, offset: Int, limit: Int): List<SpotifyPlaylist>
    suspend fun findPlaylist(id: String): SpotifyPlaylist?
    suspend fun getPlaylistTracks(playlistId: String): List<SpotifyTrack>?

    /**
     * Throw when a Spotify API HTTP call failed.
     *
     * @property status The status code associated with the HTTP error.
     * @property reason An explanation of what caused the error.
     */
    class ApiException(
        val status: HttpStatus,
        val reason: String?
    ) : Exception() {

        override val message: String?
            get() = "[${status.value()}] $reason"
    }
}