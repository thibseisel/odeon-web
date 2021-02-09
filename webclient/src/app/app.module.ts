import { NgModule } from "@angular/core"
import { BrowserModule } from "@angular/platform-browser"
import { RouterModule, Routes } from "@angular/router"
import { PlaylistAnalysisModule } from "@playlist/playlist-analysis.module"
import { AppMaterialModule } from "@shared/app-material.module"
import { TrackAnalysisModule } from "@track/track-analysis.module"
import { AppComponent } from "./app.component"

const appRoutes: Routes = [
  {
    path: "",
    redirectTo: "tracks",
    pathMatch: "full"
  },
  {
    path: "**",
    redirectTo: "tracks"
  }
]

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppMaterialModule,
    TrackAnalysisModule,
    PlaylistAnalysisModule,
    RouterModule.forRoot(appRoutes, { relativeLinkResolution: "legacy" }),
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
