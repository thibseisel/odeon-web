import { Component, EventEmitter, Input, Output } from '@angular/core';
import { SearchResult } from "../track-models";

@Component({
  selector: 'app-track-search',
  templateUrl: './track-search.component.html',
  styles: [`
  .search-result {
    cursor: pointer;
  }`]
})
export class TrackSearchComponent {
  @Input() results: SearchResult[] = [];
  @Output() onquery = new EventEmitter<string>();
  @Output() ontrackselected = new EventEmitter<SearchResult>();

  constructor() { }

  public onQueryChanged(userQuery: string) {
    this.onquery.emit(userQuery);
  }

  public select(track: SearchResult) {
    this.ontrackselected.emit(track);
  }
}
