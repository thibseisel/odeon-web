import { Component, Input } from "@angular/core";
import { Track } from "../track-models";

@Component({
  selector: "app-track-detail",
  templateUrl: "./track-detail.component.html",
  styleUrls: ["./track-detail.component.scss"]
})
export class TrackDetailComponent {
  @Input() track?: Track;

  public get tone(): string | null {
    if (this.track) {
      const key = this.track.features.key;
      const mode = this.track.features.mode;
      return `${codeToKeyName(key)}${codeToMode(mode)}`;

    } else {
      return null;
    }
  }
}

function codeToMode(mode: number): string {
  return (mode > 0) ? "M" : "m";
}

function codeToKeyName(key: number | undefined): string {
  switch (key) {
    case 0:
      return "C";
    case 1:
      return "Db";
    case 2:
      return "D";
    case 3:
      return "Eb";
    case 4:
      return "E";
    case 5:
      return "F";
    case 6:
      return "F#";
    case 7:
      return "G";
    case 8:
      return "Ab";
    case 9:
      return "A";
    case 10:
      return "Bb";
    case 11:
      return "B";
    default:
      return "?";
  }
}