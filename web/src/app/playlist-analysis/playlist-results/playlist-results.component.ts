import { Component, OnInit } from '@angular/core';
import { RemotePlaylist } from '../remote-playlist';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { PlaylistStoreService } from '../playlist-store.service';
import { Observable, empty } from 'rxjs';
import { switchMap } from 'rxjs/operators';

@Component({
  selector: 'app-playlist-results',
  templateUrl: './playlist-results.component.html',
  styleUrls: ['./playlist-results.component.scss']
})
export class PlaylistResultsComponent implements OnInit {

  public playlists$!: Observable<readonly RemotePlaylist[]>;

  constructor(
    private router: Router,
    private currentRoute: ActivatedRoute,
    private service: PlaylistStoreService
  ) {}

  ngOnInit() {
    this.playlists$ = this.currentRoute.queryParamMap.pipe(
      switchMap((params: ParamMap) => {
        const query = params.get("name")
        if (query) {
          return this.service.searchPlaylists(query)
        } else {
          return empty()
        }
      })
    )
  }

  public select(playlist: RemotePlaylist) {
    this.router.navigate(["/playlists", playlist.id])
  }
}
