import { ChangeDetectionStrategy, Component } from "@angular/core"
import { ActivatedRoute } from "@angular/router"
import { isDefined } from "@shared/utils"
import { TrackMetadataService } from "@track/track-metadata.service"
import { Track } from "@track/track-models"
import { Observable } from "rxjs"
import { filter, switchMap } from "rxjs/operators"

@Component({
  selector: "app-track-detail",
  templateUrl: "./track-detail.component.html",
  styleUrls: ["./track-detail.component.scss"],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class TrackDetailComponent {

  readonly track$: Observable<Track> = this.currentRoute.paramMap.pipe(
    switchMap(pathParams => {
      const trackId = pathParams.get("id")
      if (!trackId) throw Error("Track id should be specified in the URL")
      return this.metadata.getTrackMetadata(trackId)
    }),
    filter(isDefined)
  )

  constructor(
    private readonly metadata: TrackMetadataService,
    private readonly currentRoute: ActivatedRoute,
  ) { }
}
