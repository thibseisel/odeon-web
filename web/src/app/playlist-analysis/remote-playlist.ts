import { ImageSpec } from "@shared/remote-models"

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
