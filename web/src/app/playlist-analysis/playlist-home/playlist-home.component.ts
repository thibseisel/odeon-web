import { Component, OnInit } from '@angular/core';
import { PlaylistStoreService } from '../playlist-store.service';
import { Subject, Observable } from 'rxjs';
import { RemotePlaylist } from '../remote-playlist';
import { switchMap, debounceTime, tap } from 'rxjs/operators';

@Component({
  selector: 'app-playlist-home',
  templateUrl: './playlist-home.component.html',
  styleUrls: ['./playlist-home.component.scss']
})
export class PlaylistHomeComponent {
  private readonly userQuery = new Subject<string>()

  constructor(private service: PlaylistStoreService) { }

  public readonly playlists$: Observable<RemotePlaylist[]> = this.userQuery.pipe(
    debounceTime(300),
    switchMap((query) => this.service.searchPlaylists(query)),
    tap((playlists) => console.log(playlists))
  )

  public updateQuery(playlistQuery: string) {
    this.userQuery.next(playlistQuery.trim())
  }

}
