import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { TrackSearchComponent } from './track-search/track-search.component';


const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    component: TrackSearchComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
