import { CommonModule } from "@angular/common"
import { HttpClientModule } from "@angular/common/http"
import { NgModule } from "@angular/core"
import { ReactiveFormsModule } from "@angular/forms"
import { RouterModule, Routes } from "@angular/router"
import { AppSharedModule } from "@shared/app-shared.module"
import { OdeonCartridgeComponent } from "@track/odeon-cartridge/odeon-cartridge.component"
import { TrackDetailComponent } from "@track/track-detail/track-detail.component"

const trackRoutes: Routes = [
  {
    path: ":id",
    component: TrackDetailComponent
  }
]

@NgModule({
  declarations: [
    TrackDetailComponent,
    OdeonCartridgeComponent,
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule.forChild(trackRoutes),
    HttpClientModule,
    AppSharedModule,
  ],
  exports: [
    RouterModule
  ]
})
export class TrackAnalysisModule { }
