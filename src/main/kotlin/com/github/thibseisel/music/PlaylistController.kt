package com.github.thibseisel.music

import com.github.thibseisel.music.client.SpotifyService
import com.github.thibseisel.music.spotify.SpotifyPlaylist
import com.github.thibseisel.music.spotify.SpotifyTrack
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

/**
 * Describe the REST API for fetching playlists information from Spotify.
 */
@RestController
@RequestMapping("/api/playlists")
internal class PlaylistController(
    private val service: SpotifyService
) {

    @GetMapping
    suspend fun searchPlaylists(
        @RequestParam("name") query: String
    ): List<Playlist> {
        val spotifyPlaylists = service.searchPlaylists(query, 0, 10)
        return spotifyPlaylists.map { it.asModel() }
    }

    @GetMapping("/{id}")
    suspend fun getPlaylistDetail(
        @PathVariable("id") playlistId: String
    ): Playlist = service.findPlaylist(playlistId)
        ?.asModel()
        ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

    @GetMapping("/{id}/tracks")
    suspend fun getPlaylistTracks(
        @PathVariable("id") playlistId: String
    ): List<SpotifyTrack> = service.getPlaylistTracks(playlistId)
        ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

    private fun SpotifyPlaylist.asModel(): Playlist = Playlist(
        id = id,
        name = name,
        description = description,
        size = tracks.total,
        owner = owner.name ?: "Unknown",
        link = externalUrls["spotify"].orEmpty(),
        images = images
    )
}