import { NgModule } from '@angular/core'
import { CommonModule } from '@angular/common'
import { TrackSearchComponent } from "@track/track-search/track-search.component"
import { TrackDetailComponent } from "@track/track-detail/track-detail.component"
import { DashboardComponent } from "@track/dashboard/dashboard.component"
import { OdeonCartridgeComponent } from "@track/odeon-cartridge/odeon-cartridge.component"
import { AppMaterialModule } from "@shared/app-material.module"
import { HttpClientModule } from "@angular/common/http"

@NgModule({
  declarations: [
    DashboardComponent,
    TrackSearchComponent,
    TrackDetailComponent,
    OdeonCartridgeComponent
  ],
  imports: [
    CommonModule,
    HttpClientModule,
    AppMaterialModule
  ]
})
export class TrackAnalysisModule { }
