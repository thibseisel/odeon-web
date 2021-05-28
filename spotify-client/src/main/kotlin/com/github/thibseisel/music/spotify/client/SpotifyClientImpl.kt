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

import com.github.thibseisel.ktor.client.oauth.OAuth2Client
import com.github.thibseisel.music.spotify.model.Album
import com.github.thibseisel.music.spotify.model.Artist
import com.github.thibseisel.music.spotify.model.AudioFeature
import com.github.thibseisel.music.spotify.model.Paging
import com.github.thibseisel.music.spotify.model.SimplifiedAlbum
import com.github.thibseisel.music.spotify.model.SimplifiedArtist
import com.github.thibseisel.music.spotify.model.SimplifiedTrack
import com.github.thibseisel.music.spotify.model.SpotifyEntity
import com.github.thibseisel.music.spotify.model.Track
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.features.ClientRequestException
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLBuilder
import io.ktor.http.Url
import io.ktor.http.pathComponents
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonArray

class SpotifyClientImpl(
    engine: HttpClientEngine,
    private val baseUrl: Url,
    authConfig: AuthConfig
) : SpotifyClient {

    private val deserializer = Json {
        ignoreUnknownKeys = true
    }

    private val http = HttpClient(engine) {
        install(OAuth2Client) {
            authEndpoint = authConfig.endpoint
            clientId = authConfig.clientId
            clientSecret = authConfig.clientSecret
        }
    }

    override suspend fun searchArtists(
        query: String,
        offset: Int,
        limit: Int
    ): Paging<SimplifiedArtist> {
        TODO("Not yet implemented")
    }

    override suspend fun searchAlbums(
        query: String,
        offset: Int,
        limit: Int
    ): Paging<SimplifiedAlbum> {
        TODO("Not yet implemented")
    }

    override suspend fun searchTracks(
        query: String,
        offset: Int,
        limit: Int
    ): Paging<SimplifiedTrack> {
        TODO("Not yet implemented")
    }

    override suspend fun getArtist(id: String): Artist? {
        return getResource("/artists/$id")
    }

    override suspend fun getSeveralArtists(ids: List<String?>): List<Artist?> {
        return getSeveralResources(ids, "/artists", "artists")
    }

    override suspend fun getAlbum(id: String): Album? {
        return getResource("/albums/$id")
    }

    override suspend fun getSeveralAlbums(ids: List<String?>): List<Album?> {
        return getSeveralResources(ids, "/albums", "albums")
    }

    override suspend fun getTrack(id: String): Track? {
        return getResource("/tracks/$id")
    }

    override suspend fun getSeveralTracks(ids: List<String?>): List<Track?> {
        return getSeveralResources(ids, "/tracks", "tracks")
    }

    override suspend fun getAudioFeature(trackId: String): AudioFeature? {
        return getResource("/audio-features/$trackId")
    }

    override suspend fun getSeveralAudioFeatures(trackIds: List<String?>): List<AudioFeature?> {
        return getSeveralResources(trackIds, "/audio-features", "audio_features")
    }

    override suspend fun getArtistAlbums(
        artistId: String,
        offset: Int,
        limit: Int
    ): Paging<SimplifiedArtist> {
        return getPaginatedResources("/artists/$artistId/albums", offset, limit)
    }

    override suspend fun getAlbumTracks(
        albumId: String,
        offset: Int,
        limit: Int
    ): Paging<SimplifiedTrack> {
        return getPaginatedResources("/albums/$albumId/tracks", offset, limit)
    }

    private fun apiEndpoint(path: String): Url {
        return URLBuilder(baseUrl).pathComponents(path).build()
    }

    private suspend inline fun <reified T : SpotifyEntity> getSeveralResources(
        ids: List<String?>,
        endpoint: String,
        property: String
    ): List<T?> {
        val jsonWrapper = http.get<JsonObject>(apiEndpoint(endpoint)) {
            parameter("ids", ids.joinToString(","))
        }
        val array = jsonWrapper[property]?.jsonArray ?: error("Property $property should be set")
        return Json.decodeFromJsonElement(array)
    }

    private suspend inline fun <reified T : SpotifyEntity> getResource(
        endpoint: String
    ): T? {
        try {
            val jsonResponse = http.get<String>(apiEndpoint(endpoint))
            return deserializer.decodeFromString(jsonResponse)
        } catch (http4xx: ClientRequestException) {
            if (http4xx.response.status == HttpStatusCode.NotFound) {
                return null
            }
            throw IllegalStateException(http4xx)
        }
    }

    private suspend inline fun <reified T : SpotifyEntity> getPaginatedResources(
        endpoint: String,
        offset: Int,
        limit: Int
    ): Paging<T> = http.get(apiEndpoint(endpoint)) {
        parameter("offset", offset)
        parameter("limit", limit)
    }
}
