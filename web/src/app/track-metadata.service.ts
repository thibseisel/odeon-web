import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, Observer, empty } from 'rxjs';
import { map } from "rxjs/operators";
import { Track } from './track-models';

@Injectable({
  providedIn: 'root'
})
export class TrackMetadataService {
  private readonly baseUrl = '/api';

  constructor(private http: HttpClient) { }

  /**
   * rawTrackSearch
   */
  public rawTrackSearch(query: string): Observable<SearchResult[]> {
    const queryParams = new HttpParams();
    queryParams.set('q', query);

    return this.http.get<SearchResult[]>(`${this.baseUrl}/search`, { params: queryParams });
  }

  /**
   * getTrackMetadata
   */
  public getTrackMetadata(trackId: string): Observable<Track> {
    // Fetch track features from server.
    return empty();
  }
}

export interface SearchResult {
  id: string,
  title: string,
  album: string,
  artist: string,
  artworkUrl?: string;
}