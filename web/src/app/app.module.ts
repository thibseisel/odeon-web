import { NgModule } from "@angular/core"
import { BrowserModule } from "@angular/platform-browser"
import { AppMaterialModule } from "@shared/app-material.module"
import { AppComponent } from "./app.component"
import { TrackAnalysisModule } from "@track/track-analysis.module"
import { PlaylistAnalysisModule } from "@playlist/playlist-analysis.module"
import { RouterModule, Routes } from '@angular/router'

const appRoutes: Routes = [
  {
    path: "",
    redirectTo: "tracks",
    pathMatch: "full"
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
    RouterModule.forRoot(appRoutes),
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
