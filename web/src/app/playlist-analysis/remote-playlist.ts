import { ImageSpec, Pitch, MusicalMode } from "@shared/remote-models"

export interface RemotePlaylist {

  /**
   * The Spotify ID for the playlist.
   */
  readonly id: string

  /**
   * The name of the playlist.
   */
  readonly name: string

  /**
   * The playlist description.
   */
  readonly description?: string

  /**
   * The total number of tracks this playlist contains.
   */
  readonly number_of_tracks: number

  /**
   * The name of the user who owns the playlist.
   */
  readonly owner: string

  /**
   * The URL to view this playlist of Spotify.
   */
  readonly link: string

  /**
   * Images for the playlist.
   * The array may be empty or contain up to three images.
   * The images are returned by size in descending order.
   *
   * _Note: if returned, the source URL for the image (`url`) is temporary and will expire in less than a day._
   */
  readonly images: ReadonlyArray<ImageSpec>
}

/**
 * Group of statistics computed from audio features of tracks in the playlist.
 */
export interface FeatureDistribution {

  /**
   * Distribution of pitch keys.
   * This is the number of occurrences of each key in the whole playlist.
   * Keys that are not represented in any track are not listed.
   */
  readonly keys: Record<Pitch, number>

  /**
   * Distribution of musical modes.
   * This is the number of occurrences of each mode in the whole playlist.
   * Modes that are not represented in any track are not listed.
   */
  readonly modes: Record<MusicalMode, number>

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
