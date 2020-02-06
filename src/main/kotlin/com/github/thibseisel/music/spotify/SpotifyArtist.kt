/*
 * Copyright 2019 Thibault Seisel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.thibseisel.music.spotify

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

open class SpotifyArtist(

    /**
     * The unique identifier of this artist on Spotify servers.
     */
    @JsonProperty("id")
    val id: String,

    /**
     * The name of this artist.
     */
    @JsonProperty("name")
    val name: String
) {
    override fun toString(): String = "spotify:artist:$id ($name)"
}

/**
 * Metadata of an artist from the Spotify API.
 */
class FullSpotifyArtist @JsonCreator constructor(

    /**
     * The unique identifier of this artist on Spotify servers.
     */
    @JsonProperty("id")
    id: String,

    /**
     * The name of this artist.
     */
    @JsonProperty("name")
    name: String,

    /**
     * The popularity of the artist.
     * The value will be between `0` and `100`, with `100` being the most popular.
     * The artist’s popularity is calculated from the popularity of all the artist’s tracks.
     */
    @JsonProperty("popularity")
    val popularity: Int,

    /**
     * A list of the genres the artist is associated with.
     * For example: "Prog Rock" , "Post-Grunge".
     * (If not yet classified, the array is empty.)
     */
    @JsonProperty("genres")
    val genres: List<String>,

    /**
     * Images of the artist in various sizes, widest first.
     */
    @JsonProperty("images")
    val images: List<SpotifyImage>

) : SpotifyArtist(id, name)

