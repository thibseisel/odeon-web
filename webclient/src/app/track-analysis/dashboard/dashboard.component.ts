import { Component } from "@angular/core"
import { Observable, Subject } from "rxjs"
import { map, scan, startWith, switchMap, debounceTime } from "rxjs/operators"
import { TrackMetadataService } from "@track/track-metadata.service"
import { SearchResult, Track } from "@track/track-models"
import { SearchState } from "@track/track-search/track-search.component"

@Component({
  selector: "app-dashboard",
  templateUrl: "./dashboard.component.html",
  styleUrls: ["./dashboard.component.scss"]
})
export class DashboardComponent {
  private readonly userQuery = new Subject<string>()
  private readonly displayedTrackId = new Subject<string>()

  /**
   * The state of the search component over time.
   * The result set is initially empty and the progress indicator is not shown.
   *
   * Whenever the search query has changed an asynchronous search is performed,
   * displaying a progress indicator until results are available.
   * If querying the results takes less than 300 milliseconds then the progress indicator is not shown.
   */
  public readonly results$: Observable<SearchState> = this.userQuery.pipe(
    switchMap((query) => this.performTrackSearch(query)),
    startWith({ loading: false, results: [] }),
    scan((current: SearchState, next: SearchState) => ({
      loading: next.loading,
      results: next.results ?? current.results
    } as SearchState))
  )

  public readonly track$: Observable<Track | undefined> = this.displayedTrackId.pipe(
    switchMap((id) => this.source.getTrackMetadata(id))
  )

  constructor(private source: TrackMetadataService) { }

  public submitQuery(userQuery: string) {
    this.userQuery.next(userQuery)
  }

  public loadTrackDetail(track: SearchResult) {
    this.displayedTrackId.next(track.id)
  }

  private performTrackSearch(query: string): Observable<SearchState> {
    function toSearchState(results: Array<SearchResult>): SearchState {
      return { loading: false, results }
    }

    const asyncResults = this.source.rawTrackSearch(query)
    return asyncResults.pipe(
      map(toSearchState),
      startWith({ loading: true }),
      debounceTime(300)
    )
  }
}
