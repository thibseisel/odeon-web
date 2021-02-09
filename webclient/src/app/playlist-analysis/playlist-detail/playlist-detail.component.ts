import { Component, TrackByFunction } from "@angular/core"
import { ActivatedRoute, ParamMap } from "@angular/router"
import { Playlist, PlaylistTrack } from "@playlist/playlist-models"
import { PlaylistStoreService } from "@playlist/playlist-store.service"
import { keyDisplayName, MusicalMode, Pitch } from "@shared/remote-models"
import { DataItem, SingleSeries } from "@swimlane/ngx-charts"
import { Observable, throwError } from "rxjs"
import { map, shareReplay, switchMap } from "rxjs/operators"
import { FeatureDistribution, DistributionRange } from "@playlist/remote-playlist"

@Component({
  selector: "app-playlist-detail",
  templateUrl: "./playlist-detail.component.html",
  styleUrls: ["./playlist-detail.component.scss"]
})
export class PlaylistDetailComponent {

  constructor(
    private readonly service: PlaylistStoreService,
    private readonly currentRoute: ActivatedRoute
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
      const keyDistribution = playlist.stats.keys

      for (const key in keyDistribution) {
        if (Object.prototype.hasOwnProperty.call(keyDistribution, key)) {
          const pitch = Number(key) as Pitch
          chartData.push({
            name: keyDisplayName(pitch),
            value: keyDistribution[pitch]
          })
        }
      }

      return chartData
    })
  )

  public modePieData$: Observable<SingleSeries> = this.playlist$.pipe(
    map(playlist => {
      const chartData = Array<DataItem>()
      const modeDistribution = playlist.stats.modes

      for (const key in modeDistribution) {
        if (Object.prototype.hasOwnProperty.call(modeDistribution, key)) {
          const mode = Number(key) as MusicalMode
          chartData.push({
            name: (mode === MusicalMode.MAJOR) ? "Major" : "minor",
            value: modeDistribution[mode]
          })
        }
      }

      return chartData
    })
  )

  public tempoChartData$: Observable<SingleSeries> = this.featureChartData(0, it => it.tempo)
  public energyChartData$: Observable<SingleSeries> = this.featureChartData(1, it => it.energy)
  public danceabilityChartData$: Observable<SingleSeries> = this.featureChartData(1, it => it.danceability)
  public valenceChartData$: Observable<SingleSeries> = this.featureChartData(1, it => it.valence)
  public acousticnessChartData$: Observable<SingleSeries> = this.featureChartData(1, it => it.acousticness)
  public livenessChartData$: Observable<SingleSeries> = this.featureChartData(1, it => it.liveness)
  public instrumentalnessChartData$: Observable<SingleSeries> = this.featureChartData(1, it => it.instrumentalness)
  public speechinessChartData$: Observable<SingleSeries> = this.featureChartData(1, it => it.speechiness)

  public readonly trackByTrackId: TrackByFunction<PlaylistTrack> = (_, track) => track.id

  private featureChartData(
    decimalDigits: number,
    selector: (stats: FeatureDistribution) => ReadonlyArray<DistributionRange>
  ): Observable<SingleSeries> {
    return this.playlist$.pipe(
      map(playlist => {
        const ranges = selector(playlist.stats)

        return ranges.map<DataItem>(dist => ({
          name: `${dist.start.toFixed(decimalDigits)} - ${dist.endExclusive.toFixed(decimalDigits)}`,
          value: dist.count
        }))
      })
    )
  }
}
