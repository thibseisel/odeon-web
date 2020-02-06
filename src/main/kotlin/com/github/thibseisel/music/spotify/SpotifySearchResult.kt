package com.github.thibseisel.music.spotify

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

class SpotifySearchResult @JsonCreator constructor(
    @JsonProperty("tracks")
    val tracks: Paging<SpotifyTrack>
)