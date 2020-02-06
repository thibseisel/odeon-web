package com.github.thibseisel.music.spotify

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * A Paging object is a subset of a collection of items of type [T], where
 * [offset] is the index of the first item from [items] in the whole collection of size [total].
 *
 * Paging objects are returned by the Spotify API when the returned number of items for a request
 * is not known in advance.
 *
 * @param T The type of the items in the collection.
 */
internal class Paging<out T> @JsonCreator constructor(

    /**
     * The requested data.
     * An item at index N in this subset is the the same as the item at index `offset + N`
     * in the whole collection.
     */
    @JsonProperty("items")
    val items: List<T>,

    /**
     * The offset of the items returned (as set in the query or by default).
     */
    @JsonProperty("offset")
    val offset: Int,

    /**
     * The URL to request the next page of results.
     * This will be `null` if this page is the last one.
     */
    @JsonProperty("next")
    val next: String?,

    /**
     * The maximum number of items in the response (as set in the query or by default).
     */
    @JsonProperty("limit")
    val limit: Int,

    /**
     * The maximum number of items available to return.
     */
    @JsonProperty("total")
    val total: Int
)