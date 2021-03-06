/*
 * Copyright 2019 Thibault Seisel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.thibseisel.music.spotify

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Audio features of a specific track from the Spotify API.
 */
class SpotifyAudioFeature @JsonCreator constructor(

    /**
     * The unique identifier of the analyzed track in Spotify servers.
     */
    @JsonProperty("id")
    override val id: String,

    /**
     * The estimated overall key of the track.
     * Pitch is encoded as a number:
     *
     * ```
     * | Key | Pitch |
     * |-----|-------|
     * |   0 | C     |
     * |   1 | C#/Db |
     * |   2 | D     |
     * |   3 | D#/Eb |
     * |   4 | E     |
     * |   5 | F     |
     * |   6 | F#/Gb |
     * |   7 | G     |
     * |   8 | G#/Ab |
     * |   9 | A     |
     * |  10 | A#/Bb |
     * |  11 | B     |
     *  -------------
     *  ```
     *
     * Value is `null` if the key is unknown.
     */
    @JsonProperty("key")
    val key: Int?,

    /**
     * Mode indicates the modality (major or minor) of a track,
     * the type of scale from which its melodic content is derived.
     *
     * The value is encoded as a number: `0` for minor, `1` for major.
     */
    @JsonProperty("mode")
    val mode: Int,

    /**
     * The overall estimated tempo of a track in beats per minute (BPM).
     * In musical terminology, tempo is the speed or pace of a given piece
     * and derives directly from the average beat duration.
     */
    @JsonProperty("tempo")
    val tempo: Float,

    /**
     * An estimated overall time signature of a track.
     * The time signature (meter) is a notational convention to specify how many beats are in each bar (or measure).
     * It ranges from `3` to `7`, indicating time signatures of "3/4" to "7/4".
     * Exceptionally, a value of 1 indicates a rather complex or changing time signature.
     */
    @JsonProperty("time_signature")
    val signature: Int,

    /**
     * The overall loudness of a track in decibels (dB).
     * Loudness values are averaged across the entire track and are useful for comparing relative loudness of tracks.
     * Loudness is the quality of a sound that is the primary psychological correlate of physical strength (amplitude).
     * Values typical range between `-60` and `0` dB.
     */
    @JsonProperty("loudness")
    val loudness: Float,

    /**
     * A confidence measure from `0.0` to `1.0` of whether the track is acoustic.
     * `1.0` represents high confidence the track is acoustic.
     */
    @JsonProperty("acousticness")
    val acousticness: Float,

    /**
     * Danceability describes how suitable a track is for dancing based on a combination of musical elements
     * including tempo, rhythm stability, beat strength, and overall regularity.
     * A value of `0.0` is least danceable and `1.0` is most danceable.
     */
    @JsonProperty("danceability")
    val danceability: Float,

    /**
     * Energy is a measure from `0.0` to `1.0` and represents a perceptual measure of intensity and activity.
     * Typically, energetic tracks feel fast, loud, and noisy.
     *
     * For example, death metal has high energy, while a Bach prelude scores low on the scale.
     * Perceptual features contributing to this attribute include dynamic range, perceived loudness, timbre, onset rate,
     * and general entropy.
     */
    @JsonProperty("energy")
    val energy: Float,

    /**
     * Predicts whether a track contains no vocals.
     * “Ooh” and “aah” sounds are treated as instrumental in this context.
     * Rap or spoken word tracks are clearly “vocal”.
     * The closer the instrumentalness value is to `1.0`, the greater likelihood the track contains no vocal content. Values above 0.5 are intended to represent instrumental tracks,
     * but confidence is higher as the value approaches `1.0`.
     */
    @JsonProperty("instrumentalness")
    val instrumentalness: Float,

    /**
     * Detects the presence of an audience in the recording.
     * Higher liveness values represent an increased probability that the track was performed live.
     * A value above `0.8` provides strong likelihood that the track is live.
     */
    @JsonProperty("liveness")
    val liveness: Float,

    /**
     * Speechiness detects the presence of spoken words in a track.
     * The more exclusively speech-like the recording (e.g. talk show, audio book, poetry),
     * the closer to `1.0` the attribute value.
     * Values above `0.66` describe tracks that are probably made entirely of spoken words.
     * Values between `0.33` and `0.66` describe tracks that may contain both music and speech,
     * either in sections or layered, including such cases as rap music.
     * Values below `0.33` most likely represent music and other non-speech-like tracks.
     */
    @JsonProperty("speechiness")
    val speechiness: Float,

    /**
     * A measure from `0.0` to `1.0` describing the musical positiveness conveyed by a track.
     * Tracks with high valence sound more positive (e.g. happy, cheerful, euphoric),
     * while tracks with low valence sound more negative (e.g. sad, depressed, angry).
     */
    @JsonProperty("valence")
    val valence: Float

) : SpotifyEntity {
    override fun toString(): String = "spotify:audio_features:$id"
}