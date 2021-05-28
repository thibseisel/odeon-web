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

package com.github.thibseisel.music.spotify.client

import com.github.thibseisel.music.spotify.model.DatePrecision
import com.github.thibseisel.music.spotify.model.Image
import io.kotest.assertions.asClue
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockEngineConfig
import io.ktor.client.engine.mock.MockRequestHandler
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url
import io.ktor.http.fullPath

internal class SpotifyClientSpec : DescribeSpec({
    describe("Get one track") {
        it("returns parsed JSON") {
            val engine = MockEngine(MockEngineConfig().apply {
                addHandler(SUCCESSFUL_AUTH)
                addHandler { req ->
                    req.method shouldBe HttpMethod.Get
                    req.url.fullPath shouldBe "/tracks/7f0vVL3xi4i78Rv5Ptn2s1"
                    respondResource("json/tracks/7f0vVL3xi4i78Rv5Ptn2s1.json")
                }
            })

            val client = SpotifyClientImpl(engine, API_BASE_URL, TEST_AUTH_CONFIG)
            client.getTrack("7f0vVL3xi4i78Rv5Ptn2s1")
                .shouldNotBeNull()
                .asClue { track ->
                    track.id shouldBe "7f0vVL3xi4i78Rv5Ptn2s1"
                    track.name shouldBe "Algorithm"
                    track.discNumber shouldBe 1
                    track.trackNumber shouldBe 1
                    track.duration shouldBe 245960L
                    track.popularity shouldBe 60
                    track.explicit shouldBe false
                    track.externalUrls shouldBe mapOf(
                        "spotify" to "https://open.spotify.com/track/7f0vVL3xi4i78Rv5Ptn2s1"
                    )
                    track.artists.shouldHaveSize(1)
                    track.artists[0].asClue { artist ->
                        artist.id shouldBe "12Chz98pHFMPJEknJQMWvI"
                        artist.name shouldBe "Muse"
                    }
                    track.album.asClue { album ->
                        album.id shouldBe "5OZgDtx180ZZPMpm36J2zC"
                        album.name shouldBe "Simulation Theory (Super Deluxe)"
                        album.releaseDate shouldBe "2018-11-09"
                        album.releaseDatePrecision shouldBe DatePrecision.DAY
                        album.images.shouldContainExactly(
                            Image("https://i.scdn.co/image/0b2a261f7bec0ed109a149316d116c15ca72e5ef", 300, 300)
                        )
                        album.externalUrls shouldBe mapOf(
                            "spotify" to "https://open.spotify.com/album/5OZgDtx180ZZPMpm36J2zC"
                        )
                    }
            }
        }

        it("return null when receiving HTTP 404") {
            val engine = MockEngine(MockEngineConfig().apply {
                addHandler(SUCCESSFUL_AUTH)
                addHandler { req ->
                    req.method shouldBe HttpMethod.Get
                    req.url.fullPath shouldBe "/tracks/7f0vVL3xi4i78Rv5Ptn2s1"
                    respondError(HttpStatusCode.NotFound, content = """
                    {
                        "status": 404,
                        "message": "Not Found"
                    }
                    """.trimIndent())
                }
            })

            val client = SpotifyClientImpl(engine, API_BASE_URL, TEST_AUTH_CONFIG)
            client.getTrack("7f0vVL3xi4i78Rv5Ptn2s1") shouldBe null
        }
    }
})

private val API_BASE_URL = Url("/")
private val TEST_AUTH_CONFIG = AuthConfig(
    endpoint = Url("/auth/token"),
    clientId = "client",
    clientSecret = "secret"
)

private val SUCCESSFUL_AUTH: MockRequestHandler = { req ->
    req.method shouldBe HttpMethod.Post
    req.url.fullPath shouldBe "/auth/token"
    respond(
        status = HttpStatusCode.Created,
        content = """
        {
            "access_token": "MTQ0NjJkZmQ5OTM2NDE1ZTZjNGZmZjI3",
            "token_type": "bearer",
            "expires_in": 3600
        }
        """.trimIndent()
    )
}
