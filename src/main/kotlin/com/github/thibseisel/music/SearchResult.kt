package com.github.thibseisel.music

import com.fasterxml.jackson.annotation.JsonProperty
import com.github.thibseisel.music.spotify.SpotifyImage

data class SearchResult(

    @JsonProperty("id")
    val id: String,

    @JsonProperty("title")
    val title: String,

    @JsonProperty("artist")
    val artist: String,

    @JsonProperty("album")
    val album: String,

    @JsonProperty("artworks")
    val artworks: List<SpotifyImage>
)