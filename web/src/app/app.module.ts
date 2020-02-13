import { HttpClientModule } from "@angular/common/http";
import { NgModule } from "@angular/core";
import { BrowserModule } from "@angular/platform-browser";
import { AppMaterialModule } from "./app-material.module";
import { AppRoutingModule } from "./app-routing.module";
import { AppComponent } from "./app.component";
import { DashboardComponent } from "./dashboard/dashboard.component";
import { OdeonCartridgeComponent } from './odeon-cartridge/odeon-cartridge.component';
import { TrackDetailComponent } from "./track-detail/track-detail.component";
import { TrackSearchComponent } from "./track-search/track-search.component";

@NgModule({
  declarations: [
    AppComponent,
    DashboardComponent,
    TrackSearchComponent,
    TrackDetailComponent,
    OdeonCartridgeComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    AppMaterialModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
