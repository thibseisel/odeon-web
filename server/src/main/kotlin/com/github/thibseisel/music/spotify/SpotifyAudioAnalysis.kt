package com.github.thibseisel.music.spotify

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * An Audio Analysis describes a track's structure and musical content, including rhythm, pitch and timbre.
 * All information is precise to the audio sample.
 *
 * Many elements of audio analysis include confidence values, a floating-point number ranging from `0.0` to `1.0`.
 * Confidence indicates the reliability of its corresponding attribute.
 * Elements carrying a small confidence value should be considered speculative.
 * There may not be sufficient data in the audio to compute the attribute with high certainty.
 */
class SpotifyAudioAnalysis @JsonCreator constructor(

    /**
     * The time interval of the bars throughout the track.
     * A bar (or measure) is a segment of time defined as a given number of beats.
     * Bar offsets also indicate downbeats, the first beat of the measure.
     */
    @JsonProperty("bars")
    val bars: List<SpotifyTimeInterval>,

    /**
     * The time intervals of beats throughout the track.
     * A beat is the basic time unit of a piece of music ; for example, each tick of a metronome.
     * Beats are typically multiples of tatums.
     */
    @JsonProperty("beats")
    val beats: List<SpotifyTimeInterval>,

    /**
     * Sections are defined by large variations in rhythm or timbre, e.g. chorus, verse, bridge, guitar solo, etc.
     * Each section contains its own descriptions of tempo, key, mode, time signature and loudness.
     */
    @JsonProperty("sections")
    val sections: List<SpotifySection>
)

/**
 * Represents a time interval within an [Audio Analysis][SpotifyAudioAnalysis].
 */
class SpotifyTimeInterval @JsonCreator constructor(

    /**
     * The starting point (in seconds) of the time interval.
     */
    @JsonProperty("start")
    val start: Float,

    /**
     * The duration (in seconds) of the time interval.
     */
    @JsonProperty("duration")
    val duration: Float,

    /**
     * The confidence, from `0.0` to `1.0`, of the reliability of the interval.
     * Exceptionally, a confidence of `-1` indicates "no" value: the corresponding element should be discarded.
     */
    @JsonProperty("confidence")
    val confidence: Float
)


class SpotifySection @JsonCreator constructor(

    /**
     * The starting point (in seconds) of the section.
     */
    @JsonProperty("start")
    val start: Float,

    /**
     * The duration (in seconds) of the section.
     */
    @JsonProperty("duration")
    val duration: Float,

    /**
     * The confidence, from `0.0` to `1.0`, of the reliability of the section's "designation".
     */
    @JsonProperty("confidence")
    val confidence: Float,

    /**
     * The overall loudness of the section in decibels (dB).
     * Loudness values are useful for comparing relative loudness of sections within tracks.
     */
    @JsonProperty("loudness")
    val loudness: Float,

    /**
     * The overall estimated tempo of the section in beats per minute (BPM).
     * In musical terminology, tempo is the speed or pace of a given piece
     * and derives directly from the average beat duration.
     */
    @JsonProperty("tempo")
    val tempo: Float,

    /**
     * The confidence, from `0.0` to `1.0`, of the reliability of the _tempo_.
     * Some tracks contain tempo changes or sounds which don't contain tempo (like pure speech)
     * which would correspond to a low value in this field.
     */
    @JsonProperty("tempo_confidence")
    val tempoConfidence: Float,

    /**
     * The estimated overall key of the section.
     * The values in this field ranging from `0` to `11` mapping to pitches using standard Pitch Class notation
     * (e.g. `0` = C, `1` = C#, `2` = D and so on).
     * If no key was detected, the value is `-1`.
     */
    @JsonProperty("key")
    val key: Int,

    /**
     * The confidence, from `0.0` to `1.0`, of the reliability of the _key_.
     * Songs with many key changes may correspond to low values in this field.
     */
    @JsonProperty("key_confidence")
    val keyConfidence: Float,

    /**
     * Indicates the modality (major or minor) of a track, the type of scale from which its melodic content is derived.
     * The value is `0` for "minor", `1` for "major" or `-1` for no result.
     *
     * _Note that the major key (e.g. C major) could more likely be confused with the minor key at 3 semitones lower
     * (e.g. A minor) as both keys carry the same pitches._
     */
    @JsonProperty("mode")
    val mode: Int,

    /**
     * The confidence, from `0.0` to `1.0`, of the reliability of the _mode_.
     */
    @JsonProperty("mode_confidence")
    val modeConfidence: Float,

    /**
     * An estimated overall signature of a track.
     * The time signature (meter) is a notational convention to specify how many beats are in each bar (or measure).
     *
     * The time signature ranges from `3` to `7` indicating signatures of "3/4", to "7/4".
     * A value of `-1` may indicate no time signature,
     * while a value of 1 indicates a rather complex or changing time signature.
     */
    @JsonProperty("time_signature")
    val signature: Int,

    /**
     * The confidence, from `0.0` to `1.0`, of the reliability of the _time_signature_.
     * Sections with time signature changes may correspond to low values in this field.
     */
    @JsonProperty("time_signature_confidence")
    val signatureConfidence: Float
)