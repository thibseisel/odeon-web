import { Pitch, ImageSpec, MusicalMode } from "../track-analysis/remote-models"

/**
 * Basic information on a playlist returned as a search result.
 */
export interface PlaylistResult {

  /**
   * The unique identifier of the playlist on the remote server.
   */
  readonly id: string

  /**
   * The display name of the playlist, as given by its creator.
   */
  readonly name: string

  /**
   * An optional short description of the content of the playlist.
   */
  readonly description?: string

  /**
   * The total number of tracks that are part of the playlist.
   */
  readonly trackCount: number

  /**
   * Images for the playlist.
   * The array may be empty or contain up to three images.
   * The images are returned by size in descending order.
   */
  readonly images: ReadonlyArray<ImageSpec>
}

/**
 * Detailed information of a playlist.
 * A playlist is a selection of tracks created by one or multiple Spotify users.
 */
export interface Playlist {

  /**
   * Unique identifier of the playlist on the remote server.
   */
  readonly id: string

  /**
   * The display name of the playlist, as given by its owner.
   */
  readonly name: string

  /**
   * An optional description of the playlist, as given by its owner.
   */
  readonly description?: string

  /**
   * An external URL referencing this playlist on Spotify.
   */
  readonly link: string

  /**
   * The name of the Spotify user that created the playlist.
   */
  readonly owner: string

  /**
   * An optional icon featuring the playlist.
   * This is `undefined` if none is available.
   */
  readonly iconUrl?: string

  /**
   * The list of tracks that are part of the playlist.
   */
  readonly tracks: ReadonlyArray<PlaylistTrack>

  /**
   * Statistics computed from Audio features of all tracks that are part of the playlist.
   */
  readonly stats: FeatureStats
}

/**
 * A track that is part of a Playlist.
 */
export interface PlaylistTrack {

  /**
   * Unique identifier of that track on the remote server.
   * More information may be queried for this track using this id.
   */
  readonly id: string

  /**
   * The display name of this track.
   */
  readonly name: string

  /**
   * The name of the main artist that recorded this track.
   */
  readonly artist: string

  /**
   * An optional URL to this track's album artwork.
   * This is not set if there is none.
   */
  readonly artworkUrl?: string
}

/**
 * Group of statistics computed from audio features of tracks in the playlist.
 */
export interface FeatureStats {

  /**
   * Distribution of pitch keys.
   * This is the number of occurrences of each key in the whole playlist.
   * Keys that are not represented in any track are not listed.
   */
  readonly keys: ReadonlyMap<Pitch, number>

  /**
   * Distribution of musical modes.
   * This is the number of occurrences of each mode in the whole playlist.
   * Modes that are not represented in any track are not listed.
   */
  readonly modes: ReadonlyMap<MusicalMode, number>

  /**
   * Distribution of the "tempo" audio feature.
   * Each element of this array represents the number of tracks whose tempo is within a given range.
   */
  readonly tempo: ReadonlyArray<DistributionRange>

  /**
   * Distribution of the "energy" audio feature.
   * Each element of this array represents the number of tracks whose energy is within a given range.
   */
  readonly energy: ReadonlyArray<DistributionRange>

  /**
   * Distribution of the "danceability" audio feature.
   * Each element of this array represents the number of tracks whose danceability factor is within a given range.
   */
  readonly danceability: ReadonlyArray<DistributionRange>

  /**
   * Distribution of the "valence" audio feature.
   * Each element of this array represents the number of tracks whose valence is within a given range.
   */
  readonly valence: ReadonlyArray<DistributionRange>

  /**
   * Distribution of the likeliness of tracks to be acoustic.
   */
  readonly acousticness: ReadonlyArray<DistributionRange>

  /**
   * Distribution of the likeliness of tracks to be performed live.
   */
  readonly liveness: ReadonlyArray<DistributionRange>

  /**
   * Distribution of the likeliness of tracks not having any lyrics.
   */
  readonly instrumentalness: ReadonlyArray<DistributionRange>

  /**
   * Distribution of the likeliness of tracks mostly made of speech.
   */
  readonly speechiness: ReadonlyArray<DistributionRange>
}

/**
 * The number of elements from a source statistical series whose a specific value
 * is within a range defined by `[start ; endExclusive[`.
 * This represents both the range of values and the number of occurrences.
 */
export interface DistributionRange {

  /**
   * The number of elements whose specific value falls between this category's range,
   * i.e. `start <= value < endExclusive`.
   */
  readonly count: number

  /**
   * The lower bound of the range defined by this category.
   */
  readonly start: number

  /**
   * The exclusive upper bound of the range defined by this category.
   */
  readonly endExclusive: number
}
