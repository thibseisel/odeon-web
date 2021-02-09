import { Component, OnDestroy, OnInit } from "@angular/core"
import { ActivatedRoute, NavigationExtras, Router } from "@angular/router"
import { merge, Observable, Subject, Subscription } from "rxjs"
import { debounceTime, distinctUntilChanged, map } from "rxjs/operators"

@Component({
  selector: "app-playlist-home",
  templateUrl: "./playlist-home.component.html",
  styleUrls: ["./playlist-home.component.scss"]
})
export class PlaylistHomeComponent implements OnInit, OnDestroy {
  private readonly queryOutput = new Subject<string>()
  private readonly subscriptions = new Subscription()

  constructor(
    private readonly router: Router,
    private readonly currentRoute: ActivatedRoute
  ) { }

  public readonly userQuery$: Observable<string> = merge(
    this.queryOutput,
    this.currentRoute.queryParamMap.pipe(
      map(queryParams => queryParams.get("name") ?? "")
    )
  )

  ngOnInit(): void {
    const querySubscription = this.queryOutput.pipe(
      debounceTime(300),
      distinctUntilChanged()
    ).subscribe(query => this.updatePlaylistResults(query))

    this.subscriptions.add(querySubscription)
  }

  private updatePlaylistResults(query: string) {
    const playlistSearchExtras: NavigationExtras = { replaceUrl: true }
    if (query.length > 0) {
      playlistSearchExtras.queryParams = { name: query }
    }

    void this.router.navigate(["/playlists"], playlistSearchExtras)
  }

  public updateQuery(playlistQuery: string): void {
    this.queryOutput.next(playlistQuery.trim())
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe()
    this.queryOutput.complete()
  }
}
