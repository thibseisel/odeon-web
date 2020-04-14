import { Component, OnInit } from '@angular/core';
import { PlaylistStoreService } from '../playlist-store.service';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { Observable, throwError } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { Track } from 'src/app/track-analysis/track-models';

@Component({
  selector: 'app-playlist-detail',
  templateUrl: './playlist-detail.component.html',
  styleUrls: ["./playlist-detail.component.scss"]
})
export class PlaylistDetailComponent implements OnInit {

  public tracks$!: Observable<readonly Track[]>

  constructor(
    private service: PlaylistStoreService,
    private currentRoute: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.tracks$ = this.currentRoute.paramMap.pipe(
      switchMap((routeParams: ParamMap) => {
        const playlistId = routeParams.get("id")
        if (playlistId) {
          return this.service.getPlaylistTracks(playlistId)
        } else {
          return throwError("Playlist id is mandatory, but none is specified.")
        }
      })
    )
  }

}
