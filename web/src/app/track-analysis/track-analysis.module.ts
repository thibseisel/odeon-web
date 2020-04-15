import { NgModule } from '@angular/core'
import { CommonModule } from '@angular/common'
import { TrackSearchComponent } from "./track-search/track-search.component"
import { TrackDetailComponent } from "./track-detail/track-detail.component"
import { DashboardComponent } from "./dashboard/dashboard.component"
import { OdeonCartridgeComponent } from "./odeon-cartridge/odeon-cartridge.component"
import { AppMaterialModule } from "../app-material.module"
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
