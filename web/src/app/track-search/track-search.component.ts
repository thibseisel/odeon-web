import { Component } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { concatMap, debounceTime } from 'rxjs/operators';
import { TrackMetadataService } from '../track-metadata.service';
import { SearchResult } from "../track-models";

@Component({
  selector: 'app-track-search',
  templateUrl: './track-search.component.html',
  styles: []
})
export class TrackSearchComponent {
  private query = new Subject<string>()

  public asyncResults: Observable<SearchResult[]> = this.query.pipe(
    debounceTime(300),
    concatMap((query) => this.source.rawTrackSearch(query))
  )

  constructor(private source: TrackMetadataService) { }

  public onQueryChanged(userQuery: string) {
    this.query.next(userQuery)
  }
}
