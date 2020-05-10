package com.github.thibseisel.music

import com.github.thibseisel.music.client.SpotifyService
import com.github.thibseisel.music.spotify.SpotifyTrack
import org.springframework.web.bind.annotation.*

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
        return spotifyPlaylists.map {
            Playlist(
                id = it.id,
                name = it.name,
                description = it.description,
                size = it.tracks.total,
                owner = it.owner.name ?: "Unknown",
                link = it.externalUrls["spotify"].orEmpty(),
                images = it.images
            )
        }
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