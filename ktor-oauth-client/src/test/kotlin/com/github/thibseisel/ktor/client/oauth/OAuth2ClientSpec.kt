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

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.ktor.client.engine.mock.respondOk
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.time.ExperimentalTime
import kotlin.time.TestTimeSource
import kotlin.time.seconds

@OptIn(ExperimentalTime::class)
internal class OAuth2ClientSpec : StringSpec({
    "Token should be generated before first request" {
        val http = testHttpClient {
            addHandler(SUCCESS_AUTH)
            addHandler(AUTHENTICATED_API_GET)
        }

        http.get<HttpResponse>("https://api.server.com/v1/customers")
    }

    "Token should be reused for subsequent requests" {
        val http = testHttpClient {
            addHandler(SUCCESS_AUTH)
            addHandler(AUTHENTICATED_API_GET)
            addHandler(AUTHENTICATED_API_GET)
        }

        http.get<HttpResponse>("https://api.server.com/v1/customers")
        http.get<HttpResponse>("https://api.server.com/v1/products")
    }

    "Concurrent requests should generate token only once" {
        val http = testHttpClient {
            addHandler(SUCCESS_AUTH)
            addHandler(AUTHENTICATED_API_GET)
            addHandler(AUTHENTICATED_API_GET)
        }

        coroutineScope {
            val firstRequest = async { http.get<HttpResponse>("https://api.server.com/v1/customers") }
            val secondRequest = async { http.get<HttpResponse>("https://api.server.com/v1/products") }
            firstRequest.await()
            secondRequest.await()
        }
    }

    "Authentication failure should result in an unauthenticated call" {
        val http = testHttpClient {
            addHandler(FAILED_AUTH)
            addHandler {
                it.url.host shouldBe "api.server.com"
                it.headers[HttpHeaders.Authorization].shouldBeNull()
                respondOk()
            }
        }

        http.get<HttpResponse>("https://api.server.com/v1/customers")
    }

    "Expired token should be renewed" {
        val fakeTime = TestTimeSource()
        val http = testHttpClient(fakeTime) {
            addHandler(SUCCESS_AUTH)
            addHandler(AUTHENTICATED_API_GET)
            addHandler(SUCCESS_AUTH)
            addHandler(AUTHENTICATED_API_GET)
        }

        http.get<HttpResponse>("https://api.server.com/v1/customers")
        fakeTime += 3600.seconds
        http.get<HttpResponse>("https://api.server.com/v1/customers")
    }
})
