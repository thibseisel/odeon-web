import { CommonModule } from '@angular/common'
import { HttpClientModule } from "@angular/common/http"
import { NgModule } from '@angular/core'
import { RouterModule, Routes } from '@angular/router'
import { AppMaterialModule } from "@shared/app-material.module"
import { DashboardComponent } from "@track/dashboard/dashboard.component"
import { OdeonCartridgeComponent } from "@track/odeon-cartridge/odeon-cartridge.component"
import { TrackDetailComponent } from "@track/track-detail/track-detail.component"
import { TrackSearchComponent } from "@track/track-search/track-search.component"

const trackRoutes: Routes = [
  {
    path: "tracks",
    component: DashboardComponent
  }
]

@NgModule({
  declarations: [
    DashboardComponent,
    TrackSearchComponent,
    TrackDetailComponent,
    OdeonCartridgeComponent
  ],
  imports: [
    CommonModule,
    RouterModule.forChild(trackRoutes),
    HttpClientModule,
    AppMaterialModule
  ]
})
export class TrackAnalysisModule { }
