package com.github.thibseisel.music

import com.github.thibseisel.music.client.SpotifyService
import com.github.thibseisel.music.spotify.SpotifyAudioFeature
import com.github.thibseisel.music.spotify.SpotifyPlaylist
import com.github.thibseisel.music.spotify.SpotifyTrack
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.*
import kotlin.math.floor

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

    @GetMapping("/{id}/stats")
    suspend fun getPlaylistStats(
        @PathVariable("id") playlistId: String
    ): FeatureDistribution {
        val tracks = service.getPlaylistTracks(playlistId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        val trackIds = tracks.map { it.id }

        val features = service.getSeveralAudioFeatures(trackIds).filterNotNull()
        return FeatureDistribution(
            keys = features.groupingBy { it.key }.eachCountTo(TreeMap()),
            modes = features.groupingBy { it.mode }.eachCountTo(TreeMap()),
            tempo = features.groupByRange({ it.tempo }, 0f, 240f, 15f),
            energy = features.groupByPercentRange { it.energy },
            danceability = features.groupByPercentRange { it.danceability },
            valence = features.groupByPercentRange { it.valence },
            acousticness = features.groupByPercentRange { it.acousticness },
            liveness = features.groupByPercentRange { it.liveness },
            instrumentalness = features.groupByPercentRange { it.instrumentalness },
            speechiness = features.groupByPercentRange { it.speechiness }
        )
    }

    /**
     * Compute the distribution of values of a specific audio feature from the source set of `features`.
     *
     * For example, a call to
     * ```
     * groupByRange(features, (feature) => feature.tempo, 60, 180, 15)
     * ```
     * will produce a list that looks like the following (`count` values depend on the source set of features):
     * ```
     * [
     *  { start: 60, endExclusive: 75, count: 0 },
     *  { start: 75, endExclusive: 90, count: 14 },
     *  { start: 90, endExclusive: 105, count: 29 },
     *  { start: 105, endExclusive: 120, count: 42 },
     *  { start: 120, endExclusive: 135, count: 37 },
     *  { start: 135, endExclusive: 150, count: 21 },
     *  { start: 150, endExclusive: 165, count: 13 },
     *  { start: 165, endExclusive: 180, count: 4 },
     * ]
     * ```
     *
     * @receiver The source set of track audio features.
     * @param selector A function that selects the value of the specific audio feature whose repartition should be computed.
     * @param min The minimum considered value of the resulting distribution.
     * Any feature whose value is less than `min` will fall in the lowest category.
     * @param max The maximum considered value of the resulting distribution.
     * Any value whose value is more than `max` will fall in the highest category.
     * @param step The width of each range of the distribution.
     */
    private fun List<SpotifyAudioFeature>.groupByRange(
        selector: (SpotifyAudioFeature) -> Float,
        min: Float,
        max: Float,
        step: Float
    ): List<DistributionRange> {
        // Due to the inherent imprecise nature of floating point decimals,
        // we can't use a for loop starting at min and increasing by step until max.
        // This may lead to an inexact number of columns due to accumulated approximation errors.
        val rangeCount = floor((max - min) / step).toInt()

        val ranges = List(rangeCount) { index ->
            val lowerBound = min + index * step
            DistributionRange(
                count = 0,
                start = lowerBound,
                endExclusive = lowerBound + step
            )
        }

        for (feature in this) {
            val value = selector(feature).coerceIn(min, max)
            val rangeIndex = floor((value - min) / step).toInt()

            val matchingRange = ranges[rangeIndex]
            matchingRange.count++
        }

        return ranges
    }

    private fun List<SpotifyAudioFeature>.groupByPercentRange(
        selector: (SpotifyAudioFeature) -> Float
    ) = groupByRange(selector, 0f, 1f, .1f)
}
