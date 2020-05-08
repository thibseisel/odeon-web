import { HttpClient, HttpErrorResponse, HttpParams } from "@angular/common/http"
import { Injectable } from "@angular/core"
import { MonoTypeOperatorFunction, Observable, of, zip } from "rxjs"
import { catchError, map } from "rxjs/operators"
import { environment } from "@config/environment"
import { AudioFeature, ImageSpec, RemoteTrack } from "@track/remote-models"
import { retryAfter } from "./rx-operators"
import { SearchResult, Track } from "@track/track-models"

@Injectable({
  providedIn: "root"
})
export class TrackMetadataService {

  /**
   * The root path of all endpoints.
   */
  private readonly baseUrl = environment.apiBase

  constructor(private http: HttpClient) { }

  /**
   * Lists tracks that match the provided query.
   *
   * @param query The query that describes the search criteria.
   * @returns An asynchronous list of tracks that match the query.
   */
  public rawTrackSearch(query: string): Observable<Array<SearchResult>> {
    if (query.length > 0) {
      const queryParams = new HttpParams().set("q", query)

      return this.http.get<Array<SearchResult>>(`${this.baseUrl}/search`, { params: queryParams }).pipe(
        retryOnNetworkOrServerError(),
        catchError(err => {
          console.error("Request failed", err)
          return of([])
        })
      )
    } else {
      return of([])
    }
  }

  /**
   * Retrieve the detail of a specific track.
   *
   * @param trackId The unique identifier of the desired track on the server.
   * @returns The detail of the track, exposed as an asynchronous stream.
   * The returned value will be null if there is no track with the requested id.
   */
  public getTrackMetadata(trackId: string): Observable<Track | undefined> {
    const trackUrl = `${this.baseUrl}/tracks/${trackId}`
    const featureUrl = `${this.baseUrl}/audio-features/${trackId}`

    const asyncTrack = this.http.get<RemoteTrack>(trackUrl).pipe(retryOnNetworkOrServerError())
    const asyncFeature = this.http.get<AudioFeature>(featureUrl).pipe(retryOnNetworkOrServerError())

    return zip(asyncTrack, asyncFeature).pipe(
      map(([track, feature]) => combineToTrack(track, feature)),
      catchError((httpError) => {
        console.error(`Loading track detail failed for id=${trackId}`, httpError)
        return of(undefined)
      })
    )
  }
}

function combineToTrack(track: RemoteTrack, feature: AudioFeature): Track {
  const largestArtwork = findLargestImageIn(track.album.images)

  return {
    id: track.id,
    name: track.name,
    artist: track.artists[0]?.name,
    album: track.album.name,
    trackNo: track.track_number,
    duration: track.duration,
    popularity: track.popularity,
    artworkUrl: largestArtwork?.url,
    features: feature
  }
}

function findLargestImageIn(images: Array<ImageSpec>): ImageSpec | undefined {
  function area(image: ImageSpec): number {
    if (image.width && image.height) {
      return image.width * image.height
    } else {
      return 0
    }
  }

  if (images.length > 0) {
    return images.reduce((largest, image) => {
      const largestArea = area(largest)
      const imageArea = area(image)
      return (imageArea > largestArea) ? image : largest
    })
  } else {
    return undefined
  }
}

const MAX_ATTEMPTS = 3
const INITIAL_RETRY_DELAY = 1000

function retryOnNetworkOrServerError<T>(): MonoTypeOperatorFunction<T> {
  return retryAfter(INITIAL_RETRY_DELAY, (error, attempts) => {
    if (error instanceof HttpErrorResponse) {
      const isNetworkError = error.error instanceof ErrorEvent
      const isServerError = error.status >= 500
      return (isNetworkError || isServerError) && attempts < MAX_ATTEMPTS
    } else {
      return false
    }
  })
}
