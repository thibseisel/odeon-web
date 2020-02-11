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
  private textChanges = new Subject<string>();

  private _results: SearchResult[] = [];
  get results() { return this._results; }
  @Input() set results(results: SearchResult[]) {
    this.isLoading = false;
    this._results = results;
  }

  /**
   * Emits the user's search query whenever it has changed.
   */
  @Output() onquery = new EventEmitter<string>();

  /**
   * Notify when a result has been selected in the result list.
   */
  @Output() ontrackselected = new EventEmitter<SearchResult>();

  /**
   * Whether a query is currently is progress.
   */
  public isLoading = false

  ngOnInit() {
    this.textChanges
      .pipe(
        distinctUntilChanged(),
        debounceTime(300),
        tap(() => this.isLoading = true)
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
