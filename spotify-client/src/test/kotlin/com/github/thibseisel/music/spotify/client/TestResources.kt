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

import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpStatusCode
import java.io.FileNotFoundException

internal object TestResources {
    fun getResourceAsString(filepath: String): String {
        val classLoader = this::class.java.classLoader
            ?: error("${this.javaClass.canonicalName} has no ClassLoader")
        val resourceFile = classLoader.getResourceAsStream(filepath)
            ?: throw FileNotFoundException(filepath)
        return resourceFile.bufferedReader().use { it.readText() }
    }
}

internal fun MockRequestHandleScope.respondResource(
    resourceFilepath: String,
    status: HttpStatusCode = HttpStatusCode.OK
) = respond(
    content = TestResources.getResourceAsString(resourceFilepath),
    status = status
)
