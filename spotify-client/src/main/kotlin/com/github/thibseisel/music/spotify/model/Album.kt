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

/**
 * Metadata of an album from the Spotify API.
 */
@Serializable
data class SimplifiedAlbum(
    /**
     * The unique identifier of this album on Spotify servers.
     */
    @SerialName("id")
    override val id: String,

    /**
     * The name of the album.
     */
    @SerialName("name")
    val name: String,

    /**
     * The date the album was first released, for example `1981`.
     * Depending on the [precision][DatePrecision], it might be shown as `1981-12` or `1981-12-15`.
     */
    @SerialName("release_date")
    val releaseDate: String,

    /**
     * The precision with which release_date value is known: `year`, `month` or `day`.
     */
    @SerialName("release_date_precision")
    val releaseDatePrecision: DatePrecision,

    /**
     * The cover art for the album in various sizes, widest first.
     */
    @SerialName("images")
    val images: List<Image>,

    /**
     * Known external URLs for this album.
     */
    @SerialName("external_urls")
    val externalUrls: ExternalUrls

) : SpotifyEntity() {
    override val type: String get() = "album"
    override fun toString(): String = "$uri ($name)"
}

@Serializable
data class Album(
    /**
     * The unique identifier of this album on Spotify servers.
     */
    @SerialName("id")
    override val id: String,

    /**
     * The name of the album.
     */
    @SerialName("name")
    val name: String,

    /**
     * The date the album was first released, for example `1981`.
     * Depending on the precision, it might be shown as `1981-12` or `1981-12-15`.
     */
    @SerialName("release_date")
    val releaseDate: String,

    /**
     * The precision with which release_date value is known: year, month, or day.
     */
    @SerialName("release_date_precision")
    val releaseDatePrecision: DatePrecision,

    /**
     * The popularity of the album.
     * The value will be between 0 and 100, with 100 being the most popular.
     * The popularity is calculated from the popularity of the album's individual tracks.
     */
    @SerialName("popularity")
    val popularity: Int,

    /**
     * A list of the genres used to classify the album.
     *
     * For example, `"Prog Rock"`, `"Post-Grunge"`.
     * If not yet classified, the list is empty.
     */
    @SerialName("genres")
    val genres: List<String>,

    /**
     * The cover art for the album in various sizes, widest first.
     */
    @SerialName("images")
    val images: List<Image>,

    /**
     * Known external URLs for this album.
     */
    @SerialName("external_urls")
    val externalUrls: ExternalUrls

) : SpotifyEntity() {
    override val type: String get() = "album"
}
