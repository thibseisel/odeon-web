import { Component, OnInit } from '@angular/core'
import { PlaylistStoreService } from '../playlist-store.service'
import { ActivatedRoute, ParamMap } from '@angular/router'
import { Observable, throwError, of } from 'rxjs'
import { switchMap } from 'rxjs/operators'
import { Track } from 'src/app/track-analysis/track-models'
import { PieSeries } from "./chart-data"

@Component({
  selector: 'app-playlist-detail',
  templateUrl: './playlist-detail.component.html',
  styleUrls: ["./playlist-detail.component.scss"]
})
export class PlaylistDetailComponent implements OnInit {

  public tracks$!: Observable<ReadonlyArray<Track>>

  public pitchPieData$: Observable<PieSeries> = of([
    { name: "C", value: 12 },
    { name: "Db", value: 0 },
    { name: "D", value: 7 },
    { name: "Eb", value: 2 },
    { name: "E", value: 16 },
    { name: "F", value: 8 },
    { name: "F#", value: 1 },
    { name: "G", value: 18 },
    { name: "Ab", value: 5 },
    { name: "A", value: 15 },
    { name: "Bb", value: 7 },
    { name: "B", value: 9 }
  ])

  public modePieData$: Observable<PieSeries> = of([
    { name: "Major", value: 58 },
    { name: "minor", value: 42 }
  ])

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
