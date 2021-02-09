import { HttpClient, HttpParams } from "@angular/common/http"
import { Injectable } from "@angular/core"
import { environment } from "@config/environment"
import { Playlist, PlaylistResult } from "@playlist/playlist-models"
import { RemotePlaylist, FeatureDistribution } from "@playlist/remote-playlist"
import { ImageSpec, RemoteTrack } from "@shared/remote-models"
import { Observable, of, forkJoin } from "rxjs"
import { catchError, map } from "rxjs/operators"

@Injectable({
  providedIn: "root"
})
export class PlaylistStoreService {

  private readonly baseUrl = environment.apiBase

  constructor(private readonly http: HttpClient) { }

  /**
   * Lists playlists that match the provided `query`.
   *
   * @param query Part of the name of the playlist that should be used as the search criteria.
   * @returns An asynchronous list of playlists that match the query.
   */
  public searchPlaylists(query: string): Observable<Array<PlaylistResult>> {
    if (query.length > 0) {
      const playlistQueryParams = new HttpParams({
        fromObject: { name: query }
      })

      return this.http.get<Array<RemotePlaylist>>(`${this.baseUrl}/playlists`, { params: playlistQueryParams }).pipe(
        catchError(err => {
          console.error(`Failed to search for a playlist containing ${query}`, err)
          return of([])
        }),
        map((remoteModels: Array<RemotePlaylist>) => toPlaylistResult(remoteModels))
      )
    } else {
      return of([])
    }
  }

  /**
   * Load details of a specific playlist, with its tracks and audio feature stats.
   *
   * @param playlistId The unique identifier of the playlist to load from the remote-server.
   * @returns An observable that emits the requested playlist.
   */
  public getPlaylistDetail(playlistId: string): Observable<Playlist> {
    const playlistUrl = `${this.baseUrl}/playlists/${playlistId}`

    const asyncPlaylist = this.http.get<RemotePlaylist>(playlistUrl)
    const asyncTracks = this.http.get<Array<RemoteTrack>>(playlistUrl + "/tracks")
    const asyncStats = this.http.get<FeatureDistribution>(playlistUrl + "/stats")

    return forkJoin([asyncPlaylist, asyncTracks, asyncStats]).pipe(
      map(([playlist, tracks, stats]) => ({
        id: playlist.id,
        name: playlist.name,
        description: playlist.description,
        link: playlist.link,
        owner: playlist.owner,
        iconUrl: playlist.images[0]?.url,
        tracks: tracks.map(it => ({
          id: it.id,
          name: it.name,
          artist: it.artists[0].name,
          artworkUrl: findSmallestIcon(it.album.images)?.url
        })),
        stats
      }))
    )
  }
}

function toPlaylistResult(remotePlaylists: Array<RemotePlaylist>): Array<PlaylistResult> {
  return remotePlaylists.map(remote => ({
    id: remote.id,
    name: remote.name,
    description: remote.description,
    trackCount: remote.number_of_tracks,
    images: remote.images
  }))
}

function findSmallestIcon(icons: ReadonlyArray<ImageSpec>): ImageSpec | undefined {

  function area(image: ImageSpec): number {
    if (image.width && image.height) {
      return image.width * image.height
    } else {
      return Number.MAX_VALUE
    }
  }

  if (icons.length > 0) {
    return icons.reduce((smallest, image) => (area(image) < area(smallest)) ? image : smallest)
  } else {
    return undefined
  }
}
