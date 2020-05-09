package com.github.thibseisel.music

import com.github.thibseisel.music.spotify.SpotifyTrack
import org.springframework.web.bind.annotation.*

/**
 * Describe the REST API for fetching playlists information from Spotify.
 */
@RestController
@RequestMapping("/api/playlists")
internal class PlaylistController {

    @GetMapping
    suspend fun searchPlaylists(
        @RequestParam("name") query: String
    ): List<Playlist> {
        TODO("Fetch playlists from the search service.")
    }

    @GetMapping("/{id}")
    suspend fun getPlaylistDetail(
        @PathVariable("id") playlistId: String
    ): Playlist {
        TODO("Fetch a single playlist from the service")
    }

    @GetMapping("/{id}/tracks")
    suspend fun getPlaylistTracks(
        @PathVariable("id") playlistId: String
    ): List<SpotifyTrack> {
        TODO("Fetch playlist tracks from the service and map them to SpotifyTrack")
    }
}