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

import io.ktor.client.HttpClient
import io.ktor.client.call.receive
import io.ktor.client.features.HttpClientFeature
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.client.request.accept
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.Parameters
import io.ktor.http.Url
import io.ktor.http.isSuccess
import io.ktor.util.AttributeKey
import io.ktor.utils.io.core.use
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.time.TimeMark
import kotlin.time.TimeSource
import kotlin.time.seconds

/**
 * Configures [Ktor HttpClient][HttpClient] to automatically authenticate with an OAuth2
 * authorization server.
 *
 * Only the [Client Credentials][ClientCredentialsConfig] flow is currently supported.
 */
public class OAuth2Client private constructor(
    private val base64Key: String,
    private val authEndpoint: Url,
    private val timeSource: TimeSource
) {
    private val authMutex = Mutex()
    private var token: TokenInfo? = null

    /**
     * Configure authentication with the _Client Credentials_ flow.
     *
     * This authentication flow is often used by server-to-server applications,
     * where storing private credentials would not be considered unsafe.
     */
    public class ClientCredentialsConfig {
        /**
         * URL to the authorization server's endpoint that generates OAuth2 access tokens.
         *
         * Should not be `null`.
         */
        public var authEndpoint: Url? = null

        /**
         * Your app's OAuth2 `client_id`.
         *
         * This public key uniquely identifies apps that register with the authorization
         * server. It is generated when registering your application and its format depends on the
         * service you registered with.
         */
        public var clientId: String = ""

        /**
         * Your app's OAuth2 `client_secret`.
         *
         * This private key is only known to the authorization server and your app.
         * It is generally generated when registering your application and its format depends on
         * the service you registered with.
         */
        public var clientSecret: String = ""

        /**
         * [TimeSource] that tracks time until an access token needs to be refreshed.
         * Used for testing purposes only.
         */
        internal var timeSource: TimeSource = TimeSource.Monotonic
    }

    public companion object Feature : HttpClientFeature<ClientCredentialsConfig, OAuth2Client> {
        override val key: AttributeKey<OAuth2Client> = AttributeKey("OAuth2Client")

        override fun prepare(block: ClientCredentialsConfig.() -> Unit): OAuth2Client {
            val config = ClientCredentialsConfig().apply(block)

            require(config.clientId.isNotBlank()) { "You should specify a valid client_id" }
            require(config.clientSecret.isNotBlank()) { "You should specify a valid client_secret" }

            return OAuth2Client(
                authEndpoint = requireNotNull(config.authEndpoint) {
                    "You should specify the authorization server's url"
                },
                base64Key = "${config.clientId}:${config.clientSecret}".encodeToBase64(),
                timeSource = config.timeSource
            )
        }

        override fun install(feature: OAuth2Client, scope: HttpClient) {
            scope.requestPipeline.intercept(HttpRequestPipeline.State) {
                val token = feature.getOrGenerateToken(scope)
                if (token != null) {
                    context.headers[HttpHeaders.Authorization] = "Bearer $token"
                }
            }
        }
    }

    private suspend fun getOrGenerateToken(scope: HttpClient): String? = authMutex.withLock {
        this.token?.let { (accessToken, expirationTime) ->
            if (expirationTime.hasNotPassedNow()) {
                return accessToken
            }
        }

        val authClient = HttpClient(scope.engine) { expectSuccess = false }
        val newToken = authClient.use { authenticate(it) }
        if (newToken != null) {
            this.token = TokenInfo(
                accessToken = newToken.accessToken,
                expirationTime = timeSource.markNow() + newToken.expiresIn.seconds
            )
        }

        return newToken?.accessToken
    }

    private suspend fun authenticate(client: HttpClient): OAuth2Token? {
        val authResponse = client.post<HttpResponse>(authEndpoint) {
            accept(ContentType.Application.Json)
            header(HttpHeaders.Authorization, "Basic $base64Key")
            body = FormDataContent(Parameters.build {
                append("grant_type", "client_credentials")
            })
        }

        val jsonResponse = authResponse.receive<String>()
        return if (authResponse.status.isSuccess()) {
            Json.decodeFromString<OAuth2Token>(jsonResponse)
        } else {
            null
        }
    }

    private data class TokenInfo(
        val accessToken: String,
        val expirationTime: TimeMark
    )
}
