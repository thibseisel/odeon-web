import { Component } from '@angular/core'
import { PlaylistStoreService } from '../playlist-store.service'
import { ActivatedRoute, ParamMap } from '@angular/router'
import { Observable, throwError } from 'rxjs'
import { switchMap, shareReplay, map } from 'rxjs/operators'
import { Playlist } from "../playlist-models"
import { keyDisplayName, MusicalMode } from "src/app/track-analysis/remote-models"
import { SingleSeries, DataItem } from "@swimlane/ngx-charts"

@Component({
  selector: 'app-playlist-detail',
  templateUrl: './playlist-detail.component.html',
  styleUrls: ["./playlist-detail.component.scss"]
})
export class PlaylistDetailComponent {

  constructor(
    private service: PlaylistStoreService,
    private currentRoute: ActivatedRoute
  ) { }

  public playlist$: Observable<Playlist> = this.currentRoute.paramMap.pipe(
    switchMap((routeParams: ParamMap) => {
      const playlistId = routeParams.get("id")
      if (playlistId) {
        return this.service.getPlaylistDetail(playlistId)
      } else {
        return throwError("Playlist id is mandatory, but none is specified.")
      }
    }),
    shareReplay(1)
  )

  public pitchChartData$: Observable<SingleSeries> = this.playlist$.pipe(
    map(playlist => {
      const chartData = Array<DataItem>()
      for (const [key, count] of playlist.stats.keys) {
        chartData.push({
          name: keyDisplayName(key),
          value: count
        })
      }

      return chartData
    })
  )

  public modePieData$: Observable<SingleSeries> = this.playlist$.pipe(
    map(playlist => {
      const chartData = Array<DataItem>()
      for (const [mode, count] of playlist.stats.modes) {
        chartData.push({
          name: (mode === MusicalMode.MAJOR) ? "Major" : "minor",
          value: count
        })
      }

      return chartData
    })
  )
}
