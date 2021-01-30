import { Component, TrackByFunction } from '@angular/core'
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
export class PlaylistResultsComponent {

  constructor(
    private router: Router,
    private currentRoute: ActivatedRoute,
    private service: PlaylistStoreService
  ) { }

  /**
   * An asynchronous set of playlists whose name matches the user-provided search query.
   * That list is reloaded whenever the query changes.
   */
  public playlists$: Observable<ReadonlyArray<PlaylistResult>> = this.currentRoute.queryParamMap.pipe(
    switchMap((params: ParamMap) => {
      const query = params.get("name")
      if (query) {
        return this.service.searchPlaylists(query)
      } else {
        return EMPTY
      }
    })
  )

  public readonly trackByPlaylistId: TrackByFunction<PlaylistResult> = (_, playlist) => playlist.id

  public select(playlist: RemotePlaylist) {
    this.router.navigate(["/playlists", playlist.id])
  }
}
