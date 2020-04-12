import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { HttpClientModule } from "@angular/common/http";
import { AppMaterialModule } from "../app-material.module";
import { PlaylistHomeComponent } from './playlist-home/playlist-home.component';
import { PlaylistResultsComponent } from './playlist-results/playlist-results.component';

const playlistRoutes: Routes = [
  {
    path: "playlists",
    component: PlaylistHomeComponent,
    children: [
      {
        path: "",
        component: PlaylistResultsComponent
      }
    ]
  }
]

@NgModule({
  declarations: [PlaylistHomeComponent, PlaylistResultsComponent],
  imports: [
    CommonModule,
    RouterModule.forChild(playlistRoutes),
    HttpClientModule,
    AppMaterialModule
  ],
  exports: [
    RouterModule
  ]
})
export class PlaylistAnalysisModule { }
