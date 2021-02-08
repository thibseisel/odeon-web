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

package com.github.thibseisel.music.spotify.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A Paging object is a subset of a collection of items of type [T], where
 * [offset] is the index of the first item from [items] in the whole collection of size [total].
 *
 * Paging objects are returned by the Spotify API when the returned number of items for a request
 * is not known in advance.
 *
 * @param T The type of the items in the collection.
 */
@Serializable
class Paging<out T>(

    /**
     * The requested data.
     * An item at index N in this subset is the the same as the item at index `offset + N`
     * in the whole collection.
     */
    @SerialName("items")
    val items: List<T>,

    /**
     * The offset of the items returned (as set in the query or by default).
     */
    @SerialName("offset")
    val offset: Int,

    /**
     * The URL to request the next page of results.
     * This will be `null` if this page is the last one.
     */
    @SerialName("next")
    val next: String?,

    /**
     * The maximum number of items in the response (as set in the query or by default).
     */
    @SerialName("limit")
    val limit: Int,

    /**
     * The maximum number of items available to return.
     */
    @SerialName("total")
    val total: Int
)
