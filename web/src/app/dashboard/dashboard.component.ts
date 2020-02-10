import { Component } from "@angular/core";
import { Observable, Subject } from "rxjs";
import { concatMap } from "rxjs/operators";
import { TrackMetadataService } from "../track-metadata.service";
import { SearchResult, Track } from "../track-models";

@Component({
  selector: "app-dashboard",
  templateUrl: "./dashboard.component.html",
  styleUrls: ["./dashboard.component.scss"]
})
export class DashboardComponent {
  private userQuery = new Subject<string>();
  private displayedTrackId = new Subject<string>();

  public results$: Observable<SearchResult[]> = this.userQuery.pipe(
    concatMap((query) => this.source.rawTrackSearch(query))
  );

  public track$: Observable<Track | null> = this.displayedTrackId.pipe(
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
