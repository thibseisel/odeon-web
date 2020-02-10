import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from "@angular/core";
import { Subject } from "rxjs";
import { debounceTime, tap } from "rxjs/operators";
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

  @Output() onquery = new EventEmitter<string>();
  @Output() ontrackselected = new EventEmitter<SearchResult>();

  /**
   * Whether a query is currently is progress.
   */
  public isLoading = false

  ngOnInit() {
    this.textChanges
      .pipe(
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
