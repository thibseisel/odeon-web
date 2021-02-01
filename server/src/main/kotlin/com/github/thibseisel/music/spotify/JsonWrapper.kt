package com.github.thibseisel.music.spotify

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

class JsonWrapper<T> @JsonCreator constructor(

    @JsonProperty("data")
    @JsonAlias("tracks", "albums", "artists", "audio_features")
    val data: List<T>
)