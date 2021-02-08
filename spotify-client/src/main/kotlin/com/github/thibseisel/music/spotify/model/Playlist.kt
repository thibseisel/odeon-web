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
class Playlist(
    /**
     * The Spotify ID for the playlist.
     */
    @SerialName("id")
    override val id: String,

    /**
     * The name of the playlist.
     */
    @SerialName("name")
    val name: String,

    /**
     * The playlist description.
     * _Only set for modified, verified playlists, otherwise `null`.
     */
    @SerialName("description")
    val description: String?,

    /**
     * Known external URLs for this playlist.
     */
    @SerialName("external_urls")
    val externalUrls: ExternalUrls,

    /**
     * The user who owns the playlist.
     */
    @SerialName("owner")
    val owner: PublicUser,

    /**
     * Information about the tracks of the playlist.
     * _Note: a track may be `null` if it is no longer available_.
     */
    @SerialName("tracks")
    val tracks: List<PlaylistTrack?>,

    /**
     * Images for the playlist.
     * This list may be empty or contain up to three images.
     * The images are listed by size in descending order.
     *
     * _Note: if not `null`, the [source URL for the image][Image.url] is temporary
     * and will expire in less than a day._
     */
    @SerialName("images")
    val images: List<Image>

) : SpotifyEntity() {
    override val type: String get() = "playlist"
}

@Serializable
class SimplifiedPlaylist(

    /**
     * The Spotify ID for the playlist.
     */
    @SerialName("id")
    override val id: String,

    /**
     * The name of the playlist.
     */
    @SerialName("name")
    val name: String,

    /**
     * The playlist description.
     * _Only set for modified, verified playlists, otherwise `null`.
     */
    @SerialName("description")
    val description: String?,

    /**
     * Known external URLs for this playlist.
     */
    @SerialName("external_urls")
    val externalUrls: ExternalUrls,

    /**
     * The user who owns the playlist.
     */
    @SerialName("owner")
    val owner: PublicUser,

    /**
     * A collection containing a [link][PlaylistTracksRef.href] to the Web API endpoint
     * where full details of the playlist's track can be retrieved,
     * along with the [total][PlaylistTracksRef.total] number of tracks in the playlist.
     */
    @SerialName("tracks")
    val tracks: PlaylistTracksRef,

    /**
     * Images for the playlist.
     * This list may be empty or contain up to three images.
     * The images are listed by size in descending order.
     *
     * _Note: if not `null`, the [source URL for the image][Image.url] is temporary
     * and will expire in less than a day._
     */
    @SerialName("images")
    val images: List<Image>

) : SpotifyEntity() {
    override val type: String get() = "playlist"
}
