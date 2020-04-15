import { NgModule } from "@angular/core"
import { BrowserModule } from "@angular/platform-browser"
import { AppMaterialModule } from "./app-material.module"
import { AppRoutingModule } from "./app-routing.module"
import { AppComponent } from "./app.component"
import { TrackAnalysisModule } from "./track-analysis/track-analysis.module"
import { PlaylistAnalysisModule } from "./playlist-analysis/playlist-analysis.module"

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppMaterialModule,
    TrackAnalysisModule,
    PlaylistAnalysisModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
