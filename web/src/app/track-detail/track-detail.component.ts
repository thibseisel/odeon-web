import { Component, Input } from "@angular/core";
import { Track } from "../track-models";

@Component({
  selector: "app-track-detail",
  templateUrl: "./track-detail.component.html",
  styleUrls: ["./track-detail.component.scss"]
})
export class TrackDetailComponent {
  @Input() track?: Track;
}
