package com.github.thibseisel.music

import com.fasterxml.jackson.annotation.JsonProperty

data class SearchResult(

        @JsonProperty("id")
        val id: String,

        @JsonProperty("title")
        val title: String,

        @JsonProperty("artist")
        val artist: String,

        @JsonProperty("album")
        val album: String,

        @JsonProperty("artwork_url")
        val artworkUrl: String?
)