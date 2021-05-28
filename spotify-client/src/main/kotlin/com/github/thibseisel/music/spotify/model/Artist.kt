/*
 * Copyright 2021 Thibault SEISEL
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.thibseisel.music.spotify.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
open class SimplifiedArtist(
    /**
     * The unique identifier of this artist on Spotify servers.
     */
    @SerialName("id")
    override val id: String,

    /**
     * The name of this artist.
     */
    @SerialName("name")
    open val name: String

) : SpotifyEntity() {
    override val type: String get() = "artist"
    override fun toString(): String = "$uri ($name)"
}

/**
 * Metadata of an artist from the Spotify API.
 */
@Serializable
class Artist(
    /**
     * The unique identifier of this artist on Spotify servers.
     */
    @SerialName("id")
    override val id: String,

    /**
     * The name of this artist.
     */
    @SerialName("name")
    val name: String,

    /**
     * The popularity of the artist.
     * The value will be between `0` and `100`, with `100` being the most popular.
     * The artist’s popularity is calculated from the popularity of all the artist’s tracks.
     */
    @SerialName("popularity")
    val popularity: Int,

    /**
     * A list of the genres the artist is associated with.
     * For example: "Prog Rock" , "Post-Grunge".
     * (If not yet classified, the array is empty.)
     */
    @SerialName("genres")
    val genres: List<String>,

    /**
     * Images of the artist in various sizes, widest first.
     */
    @SerialName("images")
    val images: List<Image>,

    /**
     * Known external URLs for this artist.
     */
    @SerialName("external_urls")
    val externalUrls: ExternalUrls

) : SpotifyEntity() {
    override val type: String get() = "artist"
}
