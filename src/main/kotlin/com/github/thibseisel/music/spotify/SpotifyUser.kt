package com.github.thibseisel.music.spotify

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

internal class SpotifyUser @JsonCreator constructor(

    /**
     * The Spotify User ID for this user.
     * This is a unique string identifying the Spotify user that you can find at the end of its Spotify URI.
     * The ID of the current user can be obtained via the [Web API endpoint][https://developer.spotify.com/documentation/web-api/reference/].
     */
    @JsonProperty("id")
    val id: String,

    /**
     * The name displayed on the user's profile.
     * `null` if not available.
     */
    @JsonProperty("display_name")
    val name: String?,

    /**
     * The user's profile image.
     */
    @JsonProperty("images")
    val images: List<SpotifyImage>
)
