import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TrackMetadataComponent } from './track-metadata/track-metadata.component';


const routes: Routes = [
  {
    path: '',
    component: TrackMetadataComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
