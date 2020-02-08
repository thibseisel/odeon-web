import { Component } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { concatMap, debounceTime } from 'rxjs/operators';
import { TrackMetadataService } from '../track-metadata.service';
import { SearchResult, Track } from '../track-models';

@Component({
  selector: 'app-track-metadata',
  templateUrl: './track-metadata.component.html',
  styles: []
})
export class TrackMetadataComponent {
  private userQuery = new Subject<string>();
  private displayedTrackId = new Subject<string>();

  public results$: Observable<SearchResult[]> = this.userQuery.pipe(
    debounceTime(300),
    concatMap((query) => this.source.rawTrackSearch(query))
  );

  public track$: Observable<Track> = this.displayedTrackId.pipe(
    concatMap((id) => this.source.getTrackMetadata(id))
  );

  constructor(private source: TrackMetadataService) { }

  public submitQuery(userQuery: string) {
    this.userQuery.next(userQuery);
  }

  public loadTrackDetail(track: SearchResult) {
    this.displayedTrackId.next(track.id);
  }
}
