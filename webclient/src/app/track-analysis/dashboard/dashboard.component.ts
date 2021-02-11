import { Component, OnInit, TrackByFunction } from "@angular/core"
import { SearchResult } from "@track/track-models"
import { asyncScheduler, Subject } from "rxjs"
import { debounceTime, distinctUntilChanged, throttleTime } from "rxjs/operators"
import { DashboardStore } from "./dashboard.store"

@Component({
  selector: "app-dashboard",
  templateUrl: "./dashboard.component.html",
  styleUrls: ["./dashboard.component.scss"],
  providers: [DashboardStore]
})
export class DashboardComponent implements OnInit {

  private readonly userQuery = new Subject<string>()

  /**
   * The state of the search component over time.
   * The result set is initially empty and the progress indicator is not shown.
   *
   * Whenever the search query has changed an asynchronous search is performed,
   * displaying a progress indicator until results are available.
   * If querying the results takes less than 500 milliseconds then the progress indicator is not shown.
   */
  readonly state$ = this.store.state$.pipe(
    throttleTime(500, asyncScheduler, { leading: true, trailing: true })
  )

  readonly trackId: TrackByFunction<SearchResult> = (_, result) => result.id

  constructor(private readonly store: DashboardStore) { }

  ngOnInit(): void {
    const restrictedQuery$ = this.userQuery.pipe(
      distinctUntilChanged(),
      debounceTime(200)
    )
    this.store.search(restrictedQuery$)
  }

  submitQuery(userQuery: string): void {
    this.userQuery.next(userQuery)
  }

  loadTrackDetail(track: SearchResult): void {
    // TODO
  }
}
