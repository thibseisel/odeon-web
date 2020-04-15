import { Component, Input } from "@angular/core"
import { Track } from "../track-models"
import { Pitch, MusicalMode } from "../remote-models"

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
      return `${codeToKeyName(key)}${codeToMode(mode)}`

    } else {
      return undefined
    }
  }
}

function codeToMode(mode: MusicalMode): string {
  return (mode === MusicalMode.MAJOR) ? "M" : "m"
}

function codeToKeyName(key: Pitch | undefined): string {
  switch (key) {
    case Pitch.C:
      return "C"
    case Pitch.D_FLAT:
      return "Db"
    case Pitch.D:
      return "D"
    case Pitch.E_FLAT:
      return "Eb"
    case Pitch.E:
      return "E"
    case Pitch.F:
      return "F"
    case Pitch.F_SHARP:
      return "F#"
    case Pitch.G:
      return "G"
    case Pitch.A_FLAT:
      return "Ab"
    case Pitch.A:
      return "A"
    case Pitch.B_FLAT:
      return "Bb"
    case Pitch.B:
      return "B"
    default:
      return "?"
  }
}
