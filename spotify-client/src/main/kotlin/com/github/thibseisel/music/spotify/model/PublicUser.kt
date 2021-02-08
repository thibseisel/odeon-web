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
class PublicUser(
    /**
     * The Spotify User ID for this user.
     * This is a unique string identifying the Spotify user that you can find at the end of its Spotify URI.
     * The ID of the current user can be obtained via the [Web API endpoint][https://developer.spotify.com/documentation/web-api/reference/].
     */
    @SerialName("id")
    val id: String,

    /**
     * The name displayed on the user's profile.
     * `null` if not available.
     */
    @SerialName("display_name")
    val name: String?,

    /**
     * The user's profile image.
     */
    @SerialName("images")
    val images: List<Image>?
)
