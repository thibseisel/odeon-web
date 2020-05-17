import { HttpClient, HttpParams } from "@angular/common/http"
import { Injectable } from '@angular/core'
import { environment } from "@config/environment"
import { DistributionRange, Playlist, PlaylistResult } from "@playlist/playlist-models"
import { RemotePlaylist } from "@playlist/remote-playlist"
import { Mutable } from "@shared/language"
import { AudioFeature, ImageSpec, MusicalMode, Pitch, RemoteTrack } from "@shared/remote-models"
import { Observable, of, forkJoin } from "rxjs"
import { catchError, concatMap, map } from "rxjs/operators"

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

        return forkJoin([syncTracks, asyncFeatures])
      })
    )

    return forkJoin([asyncPlaylist, asyncFeaturedTracks]).pipe(
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

const MIN_TEMPO = 0
const MAX_TEMPO = 240
const TEMPO_STEP = 15

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
      tempo: groupByRange(features, it => it.tempo, MIN_TEMPO, MAX_TEMPO, TEMPO_STEP),
      energy: groupByPercentRange(features, it => it.energy),
      danceability: groupByPercentRange(features, it => it.danceability),
      valence: groupByPercentRange(features, it => it.valence),
      acousticness: groupByPercentRange(features, it => it.acousticness),
      instrumentalness: groupByPercentRange(features, it => it.instrumentalness),
      liveness: groupByPercentRange(features, it => it.liveness),
      speechiness: groupByPercentRange(features, it => it.speechiness)
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

/**
 * Make sure that the input number `value` always fits in the range `min` - `max`.
 */
function clamp(value: number, minValue: number, maxValue: number): number {
  return (value < minValue) ? minValue : (value > maxValue) ? maxValue : value
}

/**
 * Compute the distribution of values of a specific audio feature from the source set of `features`.
 *
 * For example, a call to
 * ```
 * groupByRange(features, (feature) => feature.tempo, 60, 180, 15)
 * ```
 * will produce an array that looks like the following (`count` values depend on the source set of features):
 * ```
 * [
 *  { start: 60, endExclusive: 75, count: 0 },
 *  { start: 75, endExclusive: 90, count: 14 },
 *  { start: 90, endExclusive: 105, count: 29 },
 *  { start: 105, endExclusive: 120, count: 42 },
 *  { start: 120, endExclusive: 135, count: 37 },
 *  { start: 135, endExclusive: 150, count: 21 },
 *  { start: 150, endExclusive: 165, count: 13 },
 *  { start: 165, endExclusive: 180, count: 4 },
 * ]
 * ```
 *
 * @param features The source set of track audio features.
 * @param selector A function that selects the value of the specific audio feature whose repartition should be computed.
 * @param min The minimum considered value of the resulting distribution.
 * Any feature whose value is less than `min` will fall in the lowest category.
 * @param max The maximum considered value of the resulting distribution.
 * Any value whose value is more than `max` will fall in the highest category.
 * @param step The width of each range of the distribution.
 */
function groupByRange(
  features: ReadonlyArray<AudioFeature>,
  selector: (feature: AudioFeature) => number,
  min: number,
  max: number,
  step: number
): ReadonlyArray<DistributionRange> {

  // Due to the inherent imprecise nature of floating point decimals,
  // we can't use a for loop starting at min and increasing by step until max.
  // This may lead to an inexact number of columns due to accumulated approximation errors.
  const rangeWidth = max - min
  const rangeCount = Math.floor(rangeWidth / step)

  const ranges = new Array<Mutable<DistributionRange>>(rangeCount)

  for (let index = 0; index < rangeCount; index++) {
    const lowerBound = min + index * step
    ranges[index] = {
      start: lowerBound,
      endExclusive: lowerBound + step,
      count: 0
    }
  }

  for (const feature of features) {
    const value = clamp(selector(feature), min, max)
    const rangeIndex = Math.floor((value - min) / step)

    const matchingRange = ranges[rangeIndex]
    matchingRange.count++
  }

  return ranges
}

const MIN_PERCENT = 0.0
const MAX_PERCENT = 1.0
const PERCENT_STEP = 0.1

function groupByPercentRange(
  features: ReadonlyArray<AudioFeature>,
  selector: (feature: AudioFeature) => number
): ReadonlyArray<DistributionRange> {
  return groupByRange(features, selector, MIN_PERCENT, MAX_PERCENT, PERCENT_STEP)
}
