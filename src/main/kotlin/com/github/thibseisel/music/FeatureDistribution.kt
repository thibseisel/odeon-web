package com.github.thibseisel.music

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Group of statistics computed from audio features of tracks.
 */
internal class FeatureDistribution(

    @JsonProperty("keys")
    val keys: Map<Int?, Int>,

    @JsonProperty("modes")
    val modes: Map<Int, Int>,

    @JsonProperty("tempo")
    val tempo: List<DistributionRange>,

    @JsonProperty("energy")
    val energy: List<DistributionRange>,

    @JsonProperty("danceability")
    val danceability: List<DistributionRange>,

    @JsonProperty("valence")
    val valence: List<DistributionRange>,

    @JsonProperty("acousticness")
    val acousticness: List<DistributionRange>,

    @JsonProperty("liveness")
    val liveness: List<DistributionRange>,

    @JsonProperty("instrumentalness")
    val instrumentalness: List<DistributionRange>,

    @JsonProperty("speechiness")
    val speechiness: List<DistributionRange>
)

/**
 * The number of elements from a source statistical series whose a specific value
 * is within a range defined by `[start ; endExclusive[`.
 * This represents both the range of values and the number of occurrences.
 */
internal class DistributionRange(

    /**
     * The number of elements whose specific value falls between this category's range,
     * i.e. `start <= value < endExclusive`.
     */
    @JsonProperty("count")
    var count: Int,

    /**
     * The lower bound of the range defined by this category.
     */
    @JsonProperty("start")
    val start: Float,

    /**
     * The exclusive upper bound of the range defined by this category.
     */
    @JsonProperty("endExclusive")
    val endExclusive: Float

    )