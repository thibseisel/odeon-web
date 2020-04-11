import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from "@angular/common/http";
import { AppMaterialModule } from "../app-material.module";
import { PlaylistHomeComponent } from './playlist-home/playlist-home.component';

@NgModule({
  declarations: [PlaylistHomeComponent],
  imports: [
    CommonModule,
    HttpClientModule,
    AppMaterialModule
  ]
})
export class PlaylistAnalysisModule { }
