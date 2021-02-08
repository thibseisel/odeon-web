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
 * The reference to a remote image hosted on Spotify.
 */
@Serializable
data class Image(

    /**
     * The source URL of the image.
     */
    @SerialName("url")
    val url: String,

    /**
     * The image width in pixels.
     * `null` if unknown.
     */
    @SerialName("width")
    val width: Int?,

    /**
     * The image height in pixels.
     * `null` if unknown.
     */
    @SerialName("height")
    val height: Int?
)
