import { Injectable } from "@angular/core"
import { ComponentStore } from "@ngrx/component-store"
import { TrackMetadataService } from "@track/track-metadata.service"
import { SearchResult } from "@track/track-models"
import { EMPTY, Observable } from "rxjs"
import { catchError, switchMap, tap } from "rxjs/operators"

export interface DashboardState {
  /**
   * Whether a search is currently pending.
   */
  readonly loading: boolean
  /**
   * The results of the last performed search.
   *
   * While `loading` is true those results are not up-to-date with the latest submitted search query
   * and only represent the result of a previous search.
   */
  readonly results: ReadonlyArray<SearchResult>
}

@Injectable()
export class DashboardStore extends ComponentStore<DashboardState> {

  constructor(
    private readonly metadataSource: TrackMetadataService
  ) {
    super({
      loading: false,
      results: []
    })
  }

  /**
   * Search for media from Spotify.
   */
  readonly search = this.effect((query$: Observable<string>) => {
    return query$.pipe(
      tap(() => this.patchState({ loading: true })),
      switchMap(query => this.metadataSource.rawTrackSearch(query)),
      tap(results => this.patchState({ results, loading: false })),
      catchError(() => EMPTY)
    )
  })
}
