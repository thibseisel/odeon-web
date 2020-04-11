import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { DashboardComponent } from "./track-analysis/dashboard/dashboard.component";
import { PlaylistHomeComponent } from "./playlist-analysis/playlist-home/playlist-home.component";


const routes: Routes = [
  {
    path: "",
    component: DashboardComponent
  },
  {
    path: "playlists",
    component: PlaylistHomeComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
