package com.github.thibseisel.music.spotify

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

internal class SpotifyPlaylistTrack @JsonCreator constructor(

    @JsonProperty("added_at")
    val addedAt: String?,

    @JsonProperty("added_by")
    val addedBy: SpotifyUser?,

    @JsonProperty("is_local")
    val isLocal: Boolean,

    @JsonProperty("track")
    val track: SpotifyTrack
)