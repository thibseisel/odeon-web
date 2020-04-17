import { Injectable } from '@angular/core'
import { HttpClient, HttpParams } from "@angular/common/http"
import { environment } from "src/environments/environment"
import { Observable, of, zip } from "rxjs"
import { RemotePlaylist } from "./remote-playlist"
import { catchError, concatMap, map } from "rxjs/operators"
import { RemoteTrack, AudioFeature, ImageSpec, Pitch, MusicalMode } from "../track-analysis/remote-models"
import { PlaylistResult, Playlist, DistributionRange } from "./playlist-models"
import { Mutable } from "../language"

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
      modes: countModeOccurences(features),
      tempo: eachInRange(features, 10, it => it.tempo),
      energy: eachInRange(features, 0.1, it => it.energy),
      danceability: eachInRange(features, 0.1, it => it.danceability),
      valence: eachInRange(features, 0.1, it => it.valence)
    }
  }
}

function countKeyOccurences(features: Array<AudioFeature>): Map<Pitch, number> {
  const occurencesPerKey = new Map<Pitch, number>()

  for (const feature of features) {
    if (feature.key) {
      const currentCountForKey = occurencesPerKey.get(feature.key) ?? 0
      occurencesPerKey.set(feature.key, currentCountForKey + 1)
    }
  }

  return occurencesPerKey
}

function countModeOccurences(features: Array<AudioFeature>): Map<MusicalMode, number> {
  const occurencesPerMode = new Map<MusicalMode, number>()

  for (const feature of features) {
    const currentCountForMode = occurencesPerMode.get(feature.mode) ?? 0
    occurencesPerMode.set(feature.mode, currentCountForMode + 1)
  }

  return occurencesPerMode
}

function eachInRange(
  source: Array<AudioFeature>,
  rangeWidth: number,
  selector: (item: AudioFeature) => number
): ReadonlyArray<DistributionRange> {

  const categories = Array<Mutable<DistributionRange>>()

  for (const el of source) {
    const feature = selector(el)
    const categoryIndex = Math.floor(feature / rangeWidth)

    const category = categories[categoryIndex]
    if (category) {
      category.count++
    } else {
      categories[categoryIndex] = {
        start: categoryIndex * rangeWidth,
        endExclusive: (categoryIndex + 1) * rangeWidth,
        count: 1
      }
    }
  }

  return categories
}
