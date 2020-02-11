package com.github.thibseisel.music

import com.github.thibseisel.music.client.SpotifyService
import com.github.thibseisel.music.spotify.FullSpotifyTrack
import com.github.thibseisel.music.spotify.SpotifyAudioFeature
import com.github.thibseisel.music.spotify.SpotifyTrack
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api")
class AudioMetadataController(
    private val service: SpotifyService
) {
    private val logger = LoggerFactory.getLogger(AudioMetadataController::class.java)

    @GetMapping("/search")
    suspend fun searchTrack(
        @RequestParam("q") query: String,
        @RequestParam("offset", defaultValue = "0") offset: Int,
        @RequestParam("limit", defaultValue = "10") limit: Int
    ): List<SearchResult> {
        val trackResults = service.search(query, offset, limit)
        return trackResults.map { track ->
            val album = track.album
            SearchResult(
                id = track.id,
                title = track.name,
                artist = track.artists.first().name,
                album = album.name,
                artworkUrl = album.images.minBy { (it.width ?: 0) * (it.height ?: 0) }?.url
            )
        }
    }

    @GetMapping("/tracks/{id}")
    suspend fun getTrack(
        @PathVariable("id") id: String
    ): FullSpotifyTrack {
        return service.findTrack(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }

    @GetMapping("/audio-features/{id}")
    suspend fun getAudioFeature(
        @PathVariable("id") trackId: String
    ): SpotifyAudioFeature {
        return service.findAudioFeature(trackId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }

    @GetMapping("/audio-features")
    suspend fun getSeveralAudioFeatures(
        @RequestParam("ids") trackIds: List<String>
    ): List<SpotifyAudioFeature?> {
        return service.getSeveralAudioFeatures(trackIds)
    }

    @ExceptionHandler
    fun handle(httpException: ResponseStatusException): ResponseEntity<ErrorPayload> {
        val errorStatus = httpException.status
        val errorPayload = ErrorPayload(errorStatus, httpException.reason ?: errorStatus.reasonPhrase)
        return ResponseEntity.status(errorStatus).body(errorPayload)
    }

    @ExceptionHandler
    fun handleUnexpected(exception: Exception): ResponseEntity<ErrorPayload> {
        logger.error("An unexpected error occurred", exception)
        val payload = ErrorPayload(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred")
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(payload)
    }
}