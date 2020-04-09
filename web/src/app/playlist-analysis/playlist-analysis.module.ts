import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from "@angular/common/http";
import { AppMaterialModule } from "../app-material.module";
import { PlaylistSearchComponent } from '../playlist-search/playlist-search.component';

@NgModule({
  declarations: [PlaylistSearchComponent],
  imports: [
    CommonModule,
    HttpClientModule,
    AppMaterialModule
  ]
})
export class PlaylistAnalysisModule { }
