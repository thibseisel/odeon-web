package com.github.thibseisel.music

import com.github.thibseisel.music.client.SpotifyService
import com.github.thibseisel.music.spotify.*
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api")
internal class AudioMetadataController(
    private val service: SpotifyService
) {

    @GetMapping("/search")
    suspend fun searchTrack(
        @RequestParam("q") query: String,
        @RequestParam("offset", defaultValue = "0") offset: Int,
        @RequestParam("limit", defaultValue = "10") limit: Int
    ): List<SearchResult> {
        val trackResults = service.searchTracks(query, offset, limit)
        return trackResults.map { track ->
            val album = track.album
            SearchResult(
                id = track.id,
                title = track.name,
                artist = track.artists.first().name,
                album = album.name,
                artworks = album.images
            )
        }
    }

    @GetMapping("/tracks")
    suspend fun getSeveralTracks(
        @RequestParam("ids") ids: List<String>
    ): List<FullSpotifyTrack?> = service.getSeveralTracks(ids)

    @GetMapping("/tracks/{id}")
    suspend fun getTrack(
        @PathVariable("id") id: String
    ): FullSpotifyTrack = service.findTrack(id)
        ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

    @GetMapping("/artists/{id}")
    suspend fun getArtist(
        @PathVariable("id") id: String
    ) : FullSpotifyArtist = service.findArtist(id)
        ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

    @GetMapping("/albums/{id}")
    suspend fun getAlbum(
        @PathVariable("id") id: String
    ) : FullSpotifyAlbum = service.findAlbum(id)
        ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

    @GetMapping("/audio-features/{id}")
    suspend fun getAudioFeature(
        @PathVariable("id") trackId: String
    ): SpotifyAudioFeature = service.findAudioFeature(trackId)
        ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

    @GetMapping("/audio-features")
    suspend fun getSeveralAudioFeatures(
        @RequestParam("ids") trackIds: List<String>
    ): List<SpotifyAudioFeature?> = service.getSeveralAudioFeatures(trackIds)

    @GetMapping("/audio-analysis/{id}")
    suspend fun getAudioAnalysis(
        @PathVariable("id") trackId: String
    ): SpotifyAudioAnalysis = service.findAudioAnalysis(trackId)
        ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
}