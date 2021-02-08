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
class PlaylistTrack(

    /**
     * The date and time the track or episode was added.
     * _Note that some very old playlists may have `null` in this field._
     */
    @SerialName("added_at")
    val addedAt: String?,

    /**
     * The Spotify user who added the track.
     * _Note that some very old playlists may have `null` in this field._
     */
    @SerialName("added_by")
    val addedBy: PublicUser?,

    /**
     * Whether this track is a local file or not.
     */
    @SerialName("is_local")
    val isLocal: Boolean,

    /**
     * Information about the track.
     */
    @SerialName("track")
    val track: Track
)
