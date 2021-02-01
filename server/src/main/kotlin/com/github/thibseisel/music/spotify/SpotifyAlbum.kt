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

/**
 * Metadata of an album from the Spotify API.
 */
open class SpotifyAlbum @JsonCreator constructor(

    /**
     * The unique identifier of this album on Spotify servers.
     */
    @JsonProperty("id")
    override val id: String,

    /**
     * The name of the album.
     */
    @JsonProperty("name")
    val name: String,

    /**
     * The date the album was first released, for example `1981`.
     * Depending on the precision, it might be shown as `1981-12` or `1981-12-15`.
     */
    @JsonProperty("release_date")
    val releaseDate: String,

    /**
     * The precision with which release_date value is known: year, month, or day.
     */
    @JsonProperty("release_date_precision")
    val releaseDatePrecision: String,

    /**
     * The cover art for the album in various sizes, widest first.
     */
    @JsonProperty("images")
    val images: List<SpotifyImage>

) : SpotifyEntity {
    override fun toString(): String = "spotify:album:$id ($name)"
}

class FullSpotifyAlbum @JsonCreator constructor(

    /**
     * The unique identifier of this album on Spotify servers.
     */
    @JsonProperty("id")
    id: String,

    /**
     * The name of the album.
     */
    @JsonProperty("name")
    name: String,

    /**
     * The date the album was first released, for example `1981`.
     * Depending on the precision, it might be shown as `1981-12` or `1981-12-15`.
     */
    @JsonProperty("release_date")
    releaseDate: String,

    /**
     * The precision with which release_date value is known: year, month, or day.
     */
    @JsonProperty("release_date_precision")
    releaseDatePrecision: String,

    /**
     * The popularity of the album.
     * The value will be between 0 and 100, with 100 being the most popular.
     * The popularity is calculated from the popularity of the album's individual tracks.
     */
    @JsonProperty("popularity")
    val popularity: Int,

    /**
     * A list of the genres used to classify the album.
     * 
     * For example, `"Prog Rock"`, `"Post-Grunge"`.
     * If not yet classified, the list is empty.
     */
    @JsonProperty("genres")
    val genres: List<String>,

    /**
     * The cover art for the album in various sizes, widest first.
     */
    @JsonProperty("images")
    images: List<SpotifyImage>

) : SpotifyAlbum(id, name, releaseDate, releaseDatePrecision, images)