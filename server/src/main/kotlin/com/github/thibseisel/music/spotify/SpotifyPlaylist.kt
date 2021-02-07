package com.github.thibseisel.music.spotify

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

internal class SpotifyPlaylist @JsonCreator constructor(

    /**
     * The Spotify ID for the playlist.
     */
    @JsonProperty("id")
    val id: String,

    /**
     * The name of the playlist.
     */
    @JsonProperty("name")
    val name: String,

    /**
     * The playlist description.
     * _Only set for modified, verified playlists, otherwise `null`.
     */
    @JsonProperty("description")
    val description: String?,

    /**
     * Known external URLs for this playlist.
     */
    @JsonProperty("external_urls")
    val externalUrls: SpotifyExternalUrls,

    /**
     * The user who owns the playlist.
     */
    @JsonProperty("owner")
    val owner: SpotifyUser,

    /**
     * Information about the tracks of the playlist.
     */
    @JsonProperty("tracks")
    val tracks: PartialPaging,

    /**
     * Images for the playlist.
     * This list may be empty or contain up to three images.
     * The images are listed by size in descending order.
     *
     * _Note: if not `null`, the [source URL for the image][SpotifyImage.url] is temporary
     * and will expire in less than a day._
     */
    @JsonProperty("images")
    val images: List<SpotifyImage>
)
