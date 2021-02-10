import { Component, EventEmitter, Input, OnDestroy, OnInit, Output, TrackByFunction } from "@angular/core"
import { SearchResult } from "@track/track-models"
import { Subject } from "rxjs"
import { debounceTime, distinctUntilChanged } from "rxjs/operators"

@Component({
  selector: "app-track-search",
  templateUrl: "./track-search.component.html",
  styleUrls: ["./track-search.component.scss"]
})
export class TrackSearchComponent implements OnInit, OnDestroy {
  private readonly textChanges = new Subject<string>()

  /**
   * The current state of the search.
   * This is marked as nullable to indicate that the initial value of the async pipe is `undefined`.
   */
  @Input() state: SearchState | undefined

  /**
   * Emits the user's search query whenever it has changed.
   */
  @Output() readonly query = new EventEmitter<string>()

  /**
   * Notify when a result has been selected in the result list.
   */
  @Output() readonly selectTrack = new EventEmitter<SearchResult>()

  public readonly trackByTrackId: TrackByFunction<SearchResult> = (_, track) => track.id

  ngOnInit(): void {
    this.textChanges
      .pipe(
        distinctUntilChanged(),
        debounceTime(500)
      )
      .subscribe((query) => this.query.emit(query))
  }

  public updateQuery(userQuery: string): void {
    this.textChanges.next(userQuery)
  }

  public submitQuery(userQuery: string): void {
    this.query.emit(userQuery)
  }

  public select(track: SearchResult): void {
    this.selectTrack.emit(track)
  }

  ngOnDestroy(): void {
    this.textChanges.complete()
  }
}

/**
 * Describes the state hold by the search component.
 */
export interface SearchState {

  /**
   * Whether a search is currently pending.
   */
  readonly loading: boolean

  /**
   * The results of the last performed search.
   * This may not present if no search results are available yet.
   *
   * When the loading is true, those results are not up-to-date with the latest submitted search query
   * and only represent the result of a previous search.
   */
  readonly results?: Array<SearchResult>
}
