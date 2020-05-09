package com.github.thibseisel.music.spotify

/**
 * An object that groups URLs to a given resource that are external to the Spotify API.
 *
 * Being expressed as a JSON object, external URLs are in fact a [Map] whose:
 * - keys are the type of URL, for example `"spotify"` is the `Spotify URL` for the resource
 * (an HTML link that opens a track, album, app, playlist or other Spotify resource in a Spotify client),
 * - values are external, public URLs to the resource.
 */
internal typealias SpotifyExternalUrls = Map<String, String>
