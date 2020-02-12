import { Component } from "@angular/core";
import { Observable, Subject, BehaviorSubject } from "rxjs";
import { map, scan, startWith, switchMap, tap } from "rxjs/operators";
import { TrackMetadataService } from "../track-metadata.service";
import { SearchResult, Track } from "../track-models";
import { SearchState } from "../track-search/track-search.component";

@Component({
  selector: "app-dashboard",
  templateUrl: "./dashboard.component.html",
  styleUrls: ["./dashboard.component.scss"]
})
export class DashboardComponent {
  private readonly userQuery = new Subject<string>();
  private readonly displayedTrackId = new Subject<string>();

  public results$: Observable<SearchState> = this.userQuery.pipe(
    switchMap((query) => this.source.rawTrackSearch(query).pipe(
      map((results) => {
        return { loading: false, results: results };
      }),
      startWith({ loading: true, results: undefined })
    )),
    startWith({ loading: false, results: [] }),
    scan((current: SearchState, next: SearchState) => {
      return {
        loading: next.loading,
        results: next?.results ?? current.results
      }
    }),
    tap((state) => console.log("Updating state", state))
  );

  public readonly track$: Observable<Track | null> = this.displayedTrackId.pipe(
    switchMap((id) => this.source.getTrackMetadata(id))
  );

  constructor(private source: TrackMetadataService) { }

  public submitQuery(userQuery: string) {
    this.userQuery.next(userQuery);
  }

  public loadTrackDetail(track: SearchResult) {
    this.displayedTrackId.next(track.id);
  }
}
