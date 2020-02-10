import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable, of } from "rxjs";
import { catchError } from "rxjs/operators";
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
      const queryParams = new HttpParams();
      queryParams.set("q", query);

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
   */
  public getTrackMetadata(trackId: string): Observable<Track> {
    const trackUrl = `${this.baseUrl}/tracks/${trackId}`;
    return this.http.get<Track>(trackUrl);
  }
}
