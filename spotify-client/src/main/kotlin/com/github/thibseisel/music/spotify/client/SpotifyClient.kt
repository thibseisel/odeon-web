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

import com.github.thibseisel.music.spotify.model.Album
import com.github.thibseisel.music.spotify.model.Artist
import com.github.thibseisel.music.spotify.model.AudioFeature
import com.github.thibseisel.music.spotify.model.Paging
import com.github.thibseisel.music.spotify.model.SimplifiedAlbum
import com.github.thibseisel.music.spotify.model.SimplifiedArtist
import com.github.thibseisel.music.spotify.model.SimplifiedTrack
import com.github.thibseisel.music.spotify.model.Track

interface SpotifyClient {
    /**
     *
     */
    suspend fun searchArtists(query: String, offset: Int, limit: Int): Paging<SimplifiedArtist>
    /**
     *
     */
    suspend fun searchAlbums(query: String, offset: Int, limit: Int): Paging<SimplifiedAlbum>

    suspend fun searchTracks(query: String, offset: Int, limit: Int): Paging<SimplifiedTrack>

    suspend fun getArtist(id: String): Artist?

    suspend fun getSeveralArtists(ids: List<String?>): List<Artist?>

    suspend fun getAlbum(id: String): Album?

    suspend fun getSeveralAlbums(ids: List<String?>): List<Album?>

    suspend fun getTrack(id: String): Track?

    suspend fun getSeveralTracks(ids: List<String?>): List<Track?>

    suspend fun getAudioFeature(trackId: String): AudioFeature?

    suspend fun getSeveralAudioFeatures(trackIds: List<String?>): List<AudioFeature?>

    suspend fun getArtistAlbums(artistId: String, offset: Int, limit: Int): Paging<SimplifiedArtist>

    suspend fun getAlbumTracks(albumId: String, offset: Int, limit: Int): Paging<SimplifiedTrack>
}
