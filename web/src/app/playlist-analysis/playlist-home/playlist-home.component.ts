import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subject, Subscription } from 'rxjs';
import { debounceTime } from 'rxjs/operators';
import { Router, NavigationExtras, Params } from '@angular/router';

@Component({
  selector: 'app-playlist-home',
  templateUrl: './playlist-home.component.html',
  styleUrls: ['./playlist-home.component.scss']
})
export class PlaylistHomeComponent implements OnInit, OnDestroy {
  private readonly userQuery = new Subject<string>()
  private subscription: Subscription | null = null

  constructor(private router: Router) {}

  ngOnInit() {
    this.subscription = this.userQuery.pipe(debounceTime(300))
      .subscribe((query) => this.updatePlaylistResults(query))
  }

  private updatePlaylistResults(query: string) {
    const playlistSearchExtras: NavigationExtras = { replaceUrl: true }
    if (query.length > 0) {
      playlistSearchExtras.queryParams = { name: query }
    }

    this.router.navigate(["/playlists"], playlistSearchExtras)
  }

  public updateQuery(playlistQuery: string) {
    this.userQuery.next(playlistQuery.trim())
  }

  ngOnDestroy() {
    this.subscription?.unsubscribe()
    this.userQuery.complete()
  }
}
