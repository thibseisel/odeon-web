package com.github.thibseisel.music

import com.fasterxml.jackson.annotation.JsonProperty
import com.github.thibseisel.music.spotify.SpotifyImage

/**
 * A playlist is a selection of tracks created by one or multiple person or entities.
 * Unlike albums, tracks that are part of a playlist may not be related in any way.
 */
internal data class Playlist(

    /**
     * The unique identifier of the playlist.
     * This may be required by other endpoints, for example to fetch the playlist's tracks.
     */
    @JsonProperty("id")
    val id: String,

    /**
     * The name given to this playlist by its owner.
     * This should be suitable for display.
     */
    @JsonProperty("name")
    val name: String,

    /**
     * An optional description of the playlist content as given by its owner.
     */
    @JsonProperty("description")
    val description: String?,

    /**
     * The number of tracks that are part of this playlist.
     */
    @JsonProperty("number_of_tracks")
    val size: Int,

    /**
     * The name of the person or entity that created this playlist.
     */
    @JsonProperty("owner")
    val owner: String,

    /**
     * An external URL that users may follow to listen to this playlist.
     */
    @JsonProperty("link")
    val link: String,

    /**
     * A set of images for this playlist.
     * This may contain up to `3` images sorted by size in descending order.
     */
    @JsonProperty("images")
    val images: List<SpotifyImage>
)