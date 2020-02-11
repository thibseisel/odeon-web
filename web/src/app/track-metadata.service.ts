import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable, of, zip } from "rxjs";
import { catchError, map } from "rxjs/operators";
import { AudioFeature, RemoteTrack, ImageSpec } from "./remote-models";
import { SearchResult, Track } from "./track-models";

@Injectable({
  providedIn: "root"
})
export class TrackMetadataService {

  /**
   * The root path of all endpoints.
   */
  private readonly baseUrl = "/api";

  constructor(private http: HttpClient) { }

  /**
   * Lists tracks that match the provided query.
   *
   * @param query The query that describes the search criteria.
   * @returns An asynchronous list of tracks that match the query.
   */
  public rawTrackSearch(query: string): Observable<SearchResult[]> {
    if (query.length > 0) {
      const queryParams = new HttpParams().set("q", query);

      return this.http.get<SearchResult[]>(`${this.baseUrl}/search`, { params: queryParams }).pipe(
        catchError(err => {
          console.error("Request failed", err);
          return of([]);
        })
      );
    } else {
      return of([]);
    }
  }

  /**
   * Retrieve the detail of a specific track.
   *
   * @param trackId The unique identifier of the desired track on the server.
   * @returns The detail of the track, exposed as an asynchronous stream.
   * The returned value will be null if there is no track with the requested id.
   */
  public getTrackMetadata(trackId: string): Observable<Track | null> {
    const trackUrl = `${this.baseUrl}/tracks/${trackId}`;
    const featureUrl = `${this.baseUrl}/audio-features/${trackId}`;

    const asyncTrack = this.http.get<RemoteTrack>(trackUrl);
    const asyncFeature = this.http.get<AudioFeature>(featureUrl);

    return zip(asyncTrack, asyncFeature).pipe(
      map(([track, feature]) => this.combineToTrack(track, feature)),
      catchError((httpError) => {
        console.error(`Loading track detail failed for id=${trackId}`, httpError);
        return of(null);
      })
    );
  }

  private combineToTrack(track: RemoteTrack, feature: AudioFeature): Track {
    const largestArtwork = this.findLargestImageIn(track.album.images);

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
    };
  }

  private findLargestImageIn(images: ImageSpec[]): ImageSpec | null {
    function area(image: ImageSpec): number {
      if (image.width && image.height) {
        return image.width * image.height;
      } else {
        return 0;
      }
    }

    if (images.length > 0) {
      return images.reduce((largest, image) => {
        const largestArea = area(largest);
        const imageArea = area(image);
        return (imageArea > largestArea) ? image : largest;
      });
    } else {
      return null;
    }
  }
}
