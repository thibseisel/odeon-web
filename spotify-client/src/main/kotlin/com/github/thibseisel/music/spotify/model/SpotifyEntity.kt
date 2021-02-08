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

abstract class SpotifyEntity {

    /**
     * The base-62 identifier that you can find at the end of the Spotify URI
     * for an artist, track, album, playlist, etc.
     * Unlike a Spotify URI, a Spotify ID does not clearly identify the type of resource.
     */
    abstract val id: String

    /**
     * The type of resource: `artist`, `album`, `track`, `playlist`, etc.
     */
    abstract val type: String

    /**
     * The resource identifier that you can enter, for example, in the Spotify Desktop client's
     * search box to locate an artist, album, or track.
     */
    val uri: String get() = "spotify:$type:$id"

    override fun toString(): String = uri
}
