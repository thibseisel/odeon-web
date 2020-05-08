import { Component, Input } from "@angular/core"
import { Track } from "@track/track-models"
import { MusicalMode, keyDisplayName } from "@track/remote-models"

@Component({
  selector: "app-track-detail",
  templateUrl: "./track-detail.component.html",
  styleUrls: ["./track-detail.component.scss"]
})
export class TrackDetailComponent {
  @Input() track?: Track

  public get tone(): string | undefined {
    if (this.track) {
      const key = this.track.features.key
      const mode = this.track.features.mode
      return `${keyDisplayName(key)}${codeToMode(mode)}`

    } else {
      return undefined
    }
  }
}

function codeToMode(mode: MusicalMode): string {
  return (mode === MusicalMode.MAJOR) ? "M" : "m"
}
