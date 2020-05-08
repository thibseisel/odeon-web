import { NgModule } from "@angular/core"
import { BrowserModule } from "@angular/platform-browser"
import { AppMaterialModule } from "@shared/app-material.module"
import { AppRoutingModule } from "./app-routing.module"
import { AppComponent } from "./app.component"
import { TrackAnalysisModule } from "@track/track-analysis.module"
import { PlaylistAnalysisModule } from "@playlist/playlist-analysis.module"

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
