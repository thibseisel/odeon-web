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
    val sections: List<SpotifySection>,

    /**
     * Audio segments attempts to subdivide a song into many segments,
     * with each segment containing a roughly consistent sound throughout its duration.
     */
    @JsonProperty("segments")
    val segments: List<SpotifySegment>,

    /**
     * A tatum represents the lowest regular pulse train that a listener
     * intuitively infers from the timing of perceived musical events (segments).
     *
     * Tatums are subdivisions of _beats_.
     */
    @JsonProperty("tatums")
    val tatums: List<SpotifyTimeInterval>
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

class SpotifySegment @JsonCreator constructor(

    /**
     * The duration (in seconds) of the segment.
     */
    @JsonProperty("start")
    val start: Float,

    /**
     * The confidence, from `0.0`to `1.0`, of the reliability of the segmentation.
     * Segments of the song which are difficult to logically segment (e.g. noise)
     * may correspond to low values in this field.
     */
    @JsonProperty("confidence")
    val confidence: Float,

    /**
     * The onset loudness of the segment in decibels (dB).
     * Combined with [loudnessMax] and [loudnessMaxTime], these components can be used to describe the "attack"
     * of the segment.
     */
    @JsonProperty("loudness_start")
    val loudnessStart: Float,

    /**
     * The peak loudness of the segment in decibels (dB).
     * Combined with [loudnessStart] and [loudnessMaxTime], these components can be used to describe the "attack"
     * of the segment.
     */
    @JsonProperty("loudness_max")
    val loudnessMax: Float,

    /**
     * The segment-relative offset of the segment peak loudness in seconds.
     * Combined with [loudnessStart] and [loudnessMax], these components can be used to describe the "attack"
     * of the segment.
     */
    @JsonProperty("loudness_max_time")
    val loudnessMaxTime: Float,

    /**
     * The offset loudness of the segment in decibels (dB).
     * This value should be equivalent to the [loudnessStart] of the following segment.
     */
    @JsonProperty("loudness_end")
    val loudnessEnd: Float,

    /**
     * A "chroma" vector representing the pitch content of the segment, corresponding to the 12 pitch classes
     * C, C#, D to B, with values ranging from `0.0` to `1.0` that describe the relative dominance
     * of every pitch in the chromatic scale.
     *
     * For example a C Major chord would likely be represented by large values of C, E and G (i.e. classes 0, 4 and 7).
     * Vectors are normalized to 1 by their strongest dimension, therefore noisy sounds are likely represented
     * by values that are all close to `1`, while pure tones are described by one value at `1` (the pitch)
     * and others near `0`.
     */
    @JsonProperty("pitches")
    val pitches: FloatArray,

    /**
     * Timbre is the quality of a music note or sound that distinguishes different types of musical instruments
     * or voices. It is a complex notion also referred to as sound color, texture, or tone quality,
     * and is derived from the shape of a segment's spectro-temporal surface,
     * independently or pitches and loudness.
     * The timbre feature is a vector that includes 12 unbounded values roughly centered around `0`.
     * Those values are high level abstractions of the spectral surface, ordered by degree of importance.
     *
     * For completeness however, the first dimension represents the average loudness of the segment,
     * second emphasizes brightness,
     * third is more closely correlated to the flatness of a sound,
     * forth to sounds with a stronger attack; etc.
     *
     * The actual timbre of the segment is best described as a linear combination of these 12 basis functions weighted
     * by the coefficient values:
     * ```
     * timbre = c1 × b1 + c2 × b2 + ... + c12 × b12
     * ```
     * where `c1` to `c12` represent the 12 coefficients and `b1` to `b12` the 12 basis functions.
     * Timbre vectors are best used in comparison with each other.
     */
    @JsonProperty("timbre")
    val timbre: FloatArray
)
