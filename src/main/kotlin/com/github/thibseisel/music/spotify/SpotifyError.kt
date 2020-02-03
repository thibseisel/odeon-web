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

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Whenever the application makes requests related to authentication or authorization to Web API,
 * such as retrieving an access token or refreshing an access token,
 * the error response follows [RFC 6749][https://tools.ietf.org/html/rfc6749]
 * on the OAuth 2.0 Authorization Framework.
 */
data class OAuthError(

    /**
     * A high level description of the error as specified
     * in [RFC 6749 Section 5.2][https://tools.ietf.org/html/rfc6749#section-5.2].
     */
    @JsonProperty("error")
    val error: String,

    /**
     * A more detailed description of the error as specified
     * in [RFC 6749 Section 4.1.2.1][https://tools.ietf.org/html/rfc6749#section-4.1.2.1].
     */
    @JsonProperty("error_description")
    val description: String?
)

/**
 * An unsuccessful response from the Spotify API.
 */
data class SpotifyError(

    /**
     * The HTTP status code that is also returned in the response header.
     */
    @JsonProperty("status")
    val status: Int,

    /**
     * A short description of the cause of the error.
     */
    @JsonProperty("message")
    val message: String?
)