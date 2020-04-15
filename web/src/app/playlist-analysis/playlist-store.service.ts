import { Injectable } from '@angular/core'
import { HttpClient, HttpParams } from "@angular/common/http"
import { environment } from "src/environments/environment"
import { Observable, of, zip } from "rxjs"
import { RemotePlaylist } from "./remote-playlist"
import { catchError, concatMap, map } from "rxjs/operators"
import { Track } from "../track-analysis/track-models"
import { RemoteTrack, AudioFeature, ImageSpec } from "../track-analysis/remote-models"
import { PlaylistResult } from "./playlist-models"

@Injectable({
  providedIn: 'root'
})
export class PlaylistStoreService {

  private readonly baseUrl = environment.apiBase

  constructor(private http: HttpClient) { }

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
        map((remoteModels: Array<RemotePlaylist>) => this.toPlaylistResult(remoteModels))
      )
    } else {
      return of([])
    }
  }

  private toPlaylistResult(remotePlaylists: Array<RemotePlaylist>): Array<PlaylistResult> {
    return remotePlaylists.map(remote => ({
      id: remote.id,
      name: remote.name,
      description: remote.description,
      trackCount: remote.number_of_tracks,
      images: remote.images
    }))
  }

  /**
   * Load detail of tracks that are part of a playlist.
   *
   * @param playlistId The unique identifier of the playlist whose tracks should be loaded.
   * @returns An asynchronous list of tracks that are part of the given playlist.
   */
  public getPlaylistTracks(playlistId: string): Observable<Array<Track>> {
    const playlistTracksUrl = `${this.baseUrl}/playlists/${playlistId}/tracks`
    const severalFeaturesUrl = `${this.baseUrl}/audio-features`

    return this.http.get<Array<RemoteTrack>>(playlistTracksUrl).pipe(
      concatMap(tracks => {
        const trackIds = tracks.map(it => it.id)
        const httpParams = new HttpParams().set("ids", trackIds.join(","))

        const asyncTracks: Observable<Array<RemoteTrack>> = of(tracks)
        const asyncFeatures = this.http.get<Array<AudioFeature>>(severalFeaturesUrl, { params: httpParams })

        return zip(asyncTracks, asyncFeatures).pipe(
          map(([remoteTracks, features]) => this.combineToTracks(remoteTracks, features))
        )
      })
    )
  }

  private combineToTracks(tracks: Array<RemoteTrack>, features: Array<AudioFeature>): Array<Track> {
    return tracks.map((it, index) => ({
        id: it.id,
        name: it.name,
        artist: it.artists[0]?.name,
        album: it.album.name,
        trackNo: it.track_number,
        duration: it.duration,
        popularity: it.popularity,
        features: features[index],
        artworkUrl: findSmallestIcon(it.album.images)?.url
      })
    )
  }
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
