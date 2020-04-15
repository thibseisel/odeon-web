import { Injectable } from '@angular/core'
import { HttpClient, HttpParams } from "@angular/common/http"
import { environment } from "src/environments/environment"
import { Observable, of, zip } from "rxjs"
import { RemotePlaylist } from "./remote-playlist"
import { catchError, concatMap, map } from "rxjs/operators"
import { RemoteTrack, AudioFeature, ImageSpec, Pitch, MusicalMode } from "../track-analysis/remote-models"
import { PlaylistResult, Playlist } from "./playlist-models"

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
        map((remoteModels: Array<RemotePlaylist>) => toPlaylistResult(remoteModels))
      )
    } else {
      return of([])
    }
  }

  /**
   * Load details of a specific playlist, with its tracks and audio feature stats.
   * @param playlistId The unique identifier of the playlist to load from the remote-server.
   */
  public getPlaylistDetail(playlistId: string): Observable<Playlist> {
    const playlistUrl = `${this.baseUrl}/playlists/${playlistId}`
    const asyncPlaylist = this.http.get<RemotePlaylist>(playlistUrl)

    const playlistTracksUrl = playlistUrl + "/tracks"
    const asyncFeaturedTracks = this.http.get<Array<RemoteTrack>>(playlistTracksUrl).pipe(
      concatMap(tracks => {
        const trackIds = tracks.map(it => it.id)
        const severalFeaturesUrl = `${this.baseUrl}/audio-features`
        const httpParams = new HttpParams().set("ids", trackIds.join(","))

        const syncTracks: Observable<Array<RemoteTrack>> = of(tracks)
        const asyncFeatures = this.http.get<Array<AudioFeature>>(severalFeaturesUrl, { params: httpParams })

        return zip(syncTracks, asyncFeatures)
      })
    )

    return zip(asyncPlaylist, asyncFeaturedTracks).pipe(
      map(([playlist, [tracks, features]]) => combineToPlaylist(playlist, tracks, features))
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

function combineToPlaylist(playlist: RemotePlaylist, tracks: Array<RemoteTrack>, features: Array<AudioFeature>): Playlist {
  return {
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
    stats: {
      keys: countKeyOccurences(features),
      modes: countModeOccurences(features)
    }
  }
}

function countKeyOccurences(features: Array<AudioFeature>): Record<Pitch, number> {
  const occurencesPerKey: Record<Pitch, number> = {
    [Pitch.C]: 0,
    [Pitch.D_FLAT]: 0,
    [Pitch.D]: 0,
    [Pitch.E_FLAT]: 0,
    [Pitch.E]: 0,
    [Pitch.F]: 0,
    [Pitch.F_SHARP]: 0,
    [Pitch.G]: 0,
    [Pitch.A_FLAT]: 0,
    [Pitch.A]: 0,
    [Pitch.B_FLAT]: 0,
    [Pitch.B]: 0
  }

  for (const feature of features) {
    if (feature.key) {
      const currentCountForKey = occurencesPerKey[feature.key]
      occurencesPerKey[feature.key] = currentCountForKey + 1
    }
  }

  return occurencesPerKey
}

function countModeOccurences(features: Array<AudioFeature>): Record<MusicalMode, number> {
  const occurencesPerMode: Record<MusicalMode, number> = {
    [MusicalMode.MINOR]: 0,
    [MusicalMode.MAJOR]: 0
  }

  for (const feature of features) {
    const currentCountForMode = occurencesPerMode[feature.mode]
    occurencesPerMode[feature.mode] = currentCountForMode
  }

  return occurencesPerMode
}
