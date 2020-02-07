import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { TrackSearchComponent } from './track-search/track-search.component';
import { TrackDetailComponent } from './track-detail/track-detail.component';


const routes: Routes = [
  {
    path: 'tracks/:id',
    component: TrackDetailComponent
  },
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
