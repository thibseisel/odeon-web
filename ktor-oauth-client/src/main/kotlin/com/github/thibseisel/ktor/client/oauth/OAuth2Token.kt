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

package com.github.thibseisel.ktor.client.oauth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A character sequence generated by an authorization server
 * that grants access to resources from the Spotify Web API.
 * An access token can be used a [certain amount of time][expiresIn] until it expires,
 * then it should be renewed by the authorization server.
 */
@Serializable
internal class OAuth2Token(

    /**
     * The access token string as issued by the authorization server.
     */
    @SerialName("access_token")
    val accessToken: String,

    /**
     * The type of token this is, typically just the string `bearer`.
     */
    @SerialName("token_type")
    val tokenType: String,

    /**
     * The number of seconds the access token is granted for.
     * An expired token cannot be used and should be renewed.
     */
    @SerialName("expires_in")
    val expiresIn: Int
)