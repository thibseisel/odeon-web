import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from "@angular/core";
import { Subject } from "rxjs";
import { debounceTime, tap, distinctUntilChanged } from "rxjs/operators";
import { SearchResult } from "../track-models";

@Component({
  selector: "app-track-search",
  templateUrl: "./track-search.component.html",
  styleUrls: ["./track-search.component.scss"]
})
export class TrackSearchComponent implements OnInit, OnDestroy {
  private readonly textChanges = new Subject<string>();

  /**
   * The current state of the search.
   * This is marked as nullable to indicate that the initial value of the async pipe is null.
   */
  @Input() state: SearchState | null = null;

  /**
   * Emits the user's search query whenever it has changed.
   */
  @Output() readonly onquery = new EventEmitter<string>();

  /**
   * Notify when a result has been selected in the result list.
   */
  @Output() readonly ontrackselected = new EventEmitter<SearchResult>();

  ngOnInit() {
    this.textChanges
      .pipe(
        distinctUntilChanged(),
        debounceTime(500)
      )
      .subscribe((query) => this.onquery.emit(query));
  }

  public updateQuery(userQuery: string) {
    this.textChanges.next(userQuery);
  }

  public submitQuery(userQuery: string) {
    this.onquery.emit(userQuery);
  }

  public select(track: SearchResult) {
    this.ontrackselected.emit(track);
  }

  ngOnDestroy() {
    this.textChanges.complete();
  }
}

/**
 * Describes the state hold by the search component.
 */
export interface SearchState {
  readonly loading: boolean;
  readonly results?: SearchResult[];
}