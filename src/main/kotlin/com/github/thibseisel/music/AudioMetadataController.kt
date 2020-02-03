package com.github.thibseisel.music

import com.github.thibseisel.music.spotify.SpotifyAudioFeature
import kotlinx.coroutines.delay
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class AudioMetadataController {

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
        delay(1000)
        return SpotifyAudioFeature(
                id = "7f0vVL3xi4i78Rv5Ptn2s1",
                key = 2,
                mode = 1,
                tempo = 170.057f,
                signature = 4,
                loudness = -4.56f,
                acousticness = 0.0125f,
                danceability = 0.522f,
                energy = 0.923f,
                instrumentalness = 0.017f,
                liveness = 0.0854f,
                speechiness = 0.0539f,
                valence = 0.595f
        )
    }

    @GetMapping("/audio-features")
    suspend fun getSeveralAudioFeatures(
            @RequestParam("track_ids") trackIds: List<String>
    ): List<SpotifyAudioFeature?> {
        delay(1000)
        return trackIds.map { null }
    }
}