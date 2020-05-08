import { NgModule } from '@angular/core'
import { CommonModule } from '@angular/common'
import { RouterModule, Routes } from '@angular/router'
import { HttpClientModule } from "@angular/common/http"
import { AppMaterialModule } from "@shared/app-material.module"
import { PlaylistHomeComponent } from '@playlist/playlist-home/playlist-home.component'
import { PlaylistResultsComponent } from '@playlist/playlist-results/playlist-results.component'
import { PlaylistDetailComponent } from '@playlist/playlist-detail/playlist-detail.component'
import { PieChartModule, BarChartModule } from '@swimlane/ngx-charts'

const playlistRoutes: Routes = [
  {
    path: "playlists",
    component: PlaylistHomeComponent,
    children: [
      {
        path: "",
        component: PlaylistResultsComponent
      },
      {
        path: ":id",
        component: PlaylistDetailComponent
      }
    ]
  }
]

@NgModule({
  declarations: [PlaylistHomeComponent, PlaylistResultsComponent, PlaylistDetailComponent],
  imports: [
    CommonModule,
    RouterModule.forChild(playlistRoutes),
    HttpClientModule,
    AppMaterialModule,
    BarChartModule,
    PieChartModule
  ],
  exports: [
    RouterModule
  ]
})
export class PlaylistAnalysisModule { }
