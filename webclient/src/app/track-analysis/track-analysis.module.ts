import { CommonModule } from "@angular/common"
import { HttpClientModule } from "@angular/common/http"
import { NgModule } from "@angular/core"
import { ReactiveFormsModule } from "@angular/forms"
import { RouterModule, Routes } from "@angular/router"
import { AppSharedModule } from "@shared/app-shared.module"
import { DashboardComponent } from "@track/dashboard/dashboard.component"
import { OdeonCartridgeComponent } from "@track/odeon-cartridge/odeon-cartridge.component"
import { TrackDetailComponent } from "@track/track-detail/track-detail.component"
import { SearchBarComponent } from "./search-bar/search-bar.component"

const trackRoutes: Routes = [
  {
    path: "tracks",
    component: DashboardComponent
  }
]

@NgModule({
  declarations: [
    DashboardComponent,
    TrackDetailComponent,
    OdeonCartridgeComponent,
    SearchBarComponent,
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule.forChild(trackRoutes),
    HttpClientModule,
    AppSharedModule,
  ]
})
export class TrackAnalysisModule { }
