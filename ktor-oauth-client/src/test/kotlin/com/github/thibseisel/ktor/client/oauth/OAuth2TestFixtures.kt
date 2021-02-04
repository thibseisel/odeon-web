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

import io.kotest.assertions.withClue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockEngineConfig
import io.ktor.client.engine.mock.MockRequestHandler
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondOk
import io.ktor.client.request.forms.FormDataContent
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url
import kotlin.time.TestTimeSource
import kotlin.time.TimeSource

internal const val ACCESS_TOKEN = "MTQ0NjJkZmQ5OTM2NDE1ZTZjNGZmZjI3"
internal val TEST_AUTH_ENDPOINT = Url("https://auth.server.com/api/token")

internal val SUCCESS_AUTH: MockRequestHandler = {
    withClue("Expected to request the authorization server, but requested ${it.url}") {
        it.method shouldBe HttpMethod.Post
        it.url shouldBe TEST_AUTH_ENDPOINT
    }

    it.headers[HttpHeaders.Authorization] shouldBe "Basic Y2xpZW50X2lkOmNsaWVudF9zZWNyZXQ="
    val body = it.body.shouldBeInstanceOf<FormDataContent>()
    body.formData["grant_type"] shouldBe "client_credentials"

    respond(
        status = HttpStatusCode.Created,
        content = """
        {
          "access_token": "$ACCESS_TOKEN",
          "token_type": "bearer",
          "expires_in": 3600
        }
        """.trimIndent()
    )
}

internal val FAILED_AUTH: MockRequestHandler = {
    withClue("Expected to request the authorization server, but requested ${it.url}") {
        it.url shouldBe TEST_AUTH_ENDPOINT
    }

    respond(
        status = HttpStatusCode.BadRequest,
        content = """
            {
              "error": "invalid_client",
              "description": "Invalid client"
            }
            """.trimIndent()
    )
}

internal val AUTHENTICATED_API_GET: MockRequestHandler = {
    withClue("Expected to request API server, but requested ${it.url}") {
        it.url.host shouldBe "api.server.com"
    }

    it.headers[HttpHeaders.Authorization] shouldBe "Bearer $ACCESS_TOKEN"
    respondOk()
}

internal fun testHttpClient(
    time: TimeSource = TestTimeSource(),
    mockResponseConfig: MockEngineConfig.() -> Unit
) = HttpClient(MockEngine) {
    engine(mockResponseConfig)
    install(OAuth2Client) {
        authEndpoint = TEST_AUTH_ENDPOINT
        clientId = "client_id"
        clientSecret = "client_secret"
        timeSource = time
    }
}
