import { Component, OnInit } from '@angular/core'
import { ActivatedRoute, ParamMap, Router } from '@angular/router'
import { PlaylistResult } from "@playlist/playlist-models"
import { PlaylistStoreService } from '@playlist/playlist-store.service'
import { RemotePlaylist } from '@playlist/remote-playlist'
import { EMPTY, Observable } from 'rxjs'
import { switchMap } from 'rxjs/operators'

@Component({
  selector: 'app-playlist-results',
  templateUrl: './playlist-results.component.html',
  styleUrls: ['./playlist-results.component.scss']
})
export class PlaylistResultsComponent implements OnInit {

  public playlists$!: Observable<ReadonlyArray<PlaylistResult>>

  constructor(
    private router: Router,
    private currentRoute: ActivatedRoute,
    private service: PlaylistStoreService
  ) { }

  ngOnInit() {
    this.playlists$ = this.currentRoute.queryParamMap.pipe(
      switchMap((params: ParamMap) => {
        const query = params.get("name")
        if (query) {
          return this.service.searchPlaylists(query)
        } else {
          return EMPTY
        }
      })
    )
  }

  public select(playlist: RemotePlaylist) {
    this.router.navigate(["/playlists", playlist.id])
  }
}
