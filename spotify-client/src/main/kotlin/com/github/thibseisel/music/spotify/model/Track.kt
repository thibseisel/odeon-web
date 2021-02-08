/*
 * Copyright 2021 Thibault SEISEL
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.thibseisel.music.spotify.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Metadata of a track from the Spotify API.
 */
@Serializable
open class SimplifiedTrack(
    /**
     * The unique identifier of this track on Spotify servers.
     */
    @SerialName("id")
    override val id: String,

    /**
     * The name of the track.
     */
    @SerialName("name")
    val name: String,

    /**
     * The disc number (usually 1 unless the album consists of more than one disc).
     */
    @SerialName("disc_number")
    val discNumber: Int,

    /**
     * The number of the track.
     * If an album has several discs, the track number is the number on the specified disc.
     */
    @SerialName("track_number")
    val trackNumber: Int,

    /**
     * The track length in milliseconds.
     */
    @SerialName("duration_ms")
    val duration: Long,

    /**
     * Whether or not the track has explicit lyrics ( true = yes it does; false = no it does not OR unknown).
     */
    @SerialName("explicit")
    val explicit: Boolean,

    /**
     * The artists whose performed the track.
     */
    @SerialName("artists")
    val artists: List<SimplifiedArtist>,

    /**
     * Known external URLs for this track.
     */
    val externalUrls: ExternalUrls

) : SpotifyEntity() {
    override val type: String get() = "track"
}

@Serializable
class Track(
    /**
     * The unique identifier of this track on Spotify servers.
     */
    @SerialName("id")
    override val id: String,

    /**
     * The name of the track.
     */
    @SerialName("name")
    val name: String,

    /**
     * The disc number (usually 1 unless the album consists of more than one disc).
     */
    @SerialName("disc_number")
    val discNumber: Int,

    /**
     * The number of the track.
     * If an album has several discs, the track number is the number on the specified disc.
     */
    @SerialName("track_number")
    val trackNumber: Int,

    /**
     * The track length in milliseconds.
     */
    @SerialName("duration_ms")
    val duration: Long,

    /**
     * The popularity of the track.
     * The value will be between 0 and 100, with 100 being the most popular.
     *
     * The popularity is calculated by algorithm and is based, in the most part,
     * on the total number of plays the track has had and how recent those plays are.
     *
     * Generally speaking, songs that are played a lot now will have a higher popularity
     * than songs that were played a lot in the past.
     * Duplicate tracks (e.g. the same track from a single and an album) are rated independently.
     * Artist and album popularity is derived mathematically from track popularity.
     * Note that the popularity value may lag actual popularity by a few days: the value is not updated in real time.
     */
    @SerialName("popularity")
    val popularity: Int,

    /**
     * Whether or not the track has explicit lyrics ( true = yes it does; false = no it does not OR unknown).
     */
    @SerialName("explicit")
    val explicit: Boolean,

    /**
     * The album album on which this track appears.
     */
    @SerialName("album")
    val album: SimplifiedAlbum,

    /**
     * The artists who performed the track.
     */
    @SerialName("artists")
    val artists: List<SimplifiedArtist>,

    /**
     * Known external URLs for this track.
     */
    val externalUrls: ExternalUrls

) : SpotifyEntity() {
    override val type: String get() = "track"
}
