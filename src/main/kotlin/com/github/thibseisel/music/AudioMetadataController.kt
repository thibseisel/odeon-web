package com.github.thibseisel.music

import com.github.thibseisel.music.client.SpotifyService
import com.github.thibseisel.music.spotify.SpotifyAudioFeature
import kotlinx.coroutines.delay
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class AudioMetadataController(
    private val service: SpotifyService
) {

    @GetMapping("/search")
    suspend fun searchTrack(
        @RequestParam("q") query: String?
    ): List<SearchResult> {
        delay(1000)
        return listOf(
            SearchResult("7f0vVL3xi4i78Rv5Ptn2s1", "Algorithm", "Muse", "Simulation Theory", null),
            SearchResult("0dMYPDqcI4ca4cjqlmp9mE", "The Dark Side", "Muse", "Simulation Theory", null),
            SearchResult("3eSyMBd7ERw68NVB3jlRmW", "Pressure", "Muse", "Simulation Theory", null)
        )
    }

    @GetMapping("/audio-features/{id}")
    suspend fun getAudioFeature(
        @PathVariable("id") trackId: String
    ): SpotifyAudioFeature {
        return service.findAudioFeature(trackId)!!
    }

    @GetMapping("/audio-features")
    suspend fun getSeveralAudioFeatures(
        @RequestParam("ids") trackIds: List<String>
    ): List<SpotifyAudioFeature?> {
        delay(1000)
        return trackIds.map { null }
    }
}