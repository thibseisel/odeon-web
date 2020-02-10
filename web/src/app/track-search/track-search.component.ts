import { Component, EventEmitter, Input, Output, OnInit, OnDestroy } from "@angular/core";
import { SearchResult } from "../track-models";
import { Subject } from "rxjs";
import { debounceTime } from "rxjs/operators";

@Component({
  selector: "app-track-search",
  templateUrl: "./track-search.component.html",
  styleUrls: ["./track-search.component.scss"]
})
export class TrackSearchComponent implements OnInit, OnDestroy {
  private textChanges = new Subject<string>();

  @Input() results: SearchResult[] = [];
  @Output() onquery = new EventEmitter<string>();
  @Output() ontrackselected = new EventEmitter<SearchResult>();

  ngOnInit() {
    this.textChanges
      .pipe(debounceTime(300))
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
