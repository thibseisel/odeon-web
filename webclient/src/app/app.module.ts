import { HttpClientModule } from "@angular/common/http"
import { NgModule } from "@angular/core"
import { ReactiveFormsModule } from "@angular/forms"
import { BrowserModule } from "@angular/platform-browser"
import { BrowserAnimationsModule } from "@angular/platform-browser/animations"
import { RouterModule, Routes } from "@angular/router"
import { AppMaterialModule } from "@shared/app-material.module"
import { AppSharedModule } from "@shared/app-shared.module"
import { AppComponent } from "./app.component"
import { HomeComponent } from "./home/home.component"
import { SearchBarComponent } from "./search-bar/search-bar.component"

const appRoutes: Routes = [
  {
    path: "tracks",
    loadChildren: () => import("./track-analysis").then(mod => mod.TrackAnalysisModule)
  },
  {
    path: "playlists",
    loadChildren: () => import("./playlist-analysis").then(mod => mod.PlaylistAnalysisModule)
  },
  {
    path: "",
    component: HomeComponent,
    pathMatch: "full"
  },
  {
    path: "**",
    redirectTo: ""
  }
]

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    SearchBarComponent,
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    RouterModule.forRoot(appRoutes),
    AppSharedModule,
    AppMaterialModule,
    ReactiveFormsModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
