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
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldBeEmpty

internal class Base64Spec : StringSpec({
    "Encode empty string to empty string" {
        "".encodeToBase64().shouldBeEmpty()
    }

    "Encode generic strings to Base64 with padding" {
        forAll(
            row("any carnal pleas", "YW55IGNhcm5hbCBwbGVhcw=="),
            row("any carnal pleasu", "YW55IGNhcm5hbCBwbGVhc3U="),
            row("any carnal pleasur", "YW55IGNhcm5hbCBwbGVhc3Vy"),
            row("any carnal pleasure", "YW55IGNhcm5hbCBwbGVhc3VyZQ=="),
        ) { sourceString, base64Encoded ->
            sourceString.encodeToBase64() shouldBe base64Encoded
        }
    }

    "Encode OAuth2 client key to Base64" {
        "845647bc2d3147c1a2d48584fc6b978c".encodeToBase64() shouldBe "ODQ1NjQ3YmMyZDMxNDdjMWEyZDQ4NTg0ZmM2Yjk3OGM="
    }
})
