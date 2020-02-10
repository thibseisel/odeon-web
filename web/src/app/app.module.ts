import { HttpClientModule } from "@angular/common/http";
import { NgModule } from "@angular/core";
import { BrowserModule } from "@angular/platform-browser";
import { HttpClientInMemoryWebApiModule } from "angular-in-memory-web-api";
import { environment } from "src/environments/environment";
import { AppMaterialModule } from "./app-material.module";
import { AppRoutingModule } from "./app-routing.module";
import { AppComponent } from "./app.component";
import { InMemoryAudioService } from "./audio.inmemory.service";
import { DashboardComponent } from "./dashboard/dashboard.component";
import { TrackDetailComponent } from "./track-detail/track-detail.component";
import { TrackSearchComponent } from "./track-search/track-search.component";
import { OdeonCartridgeComponent } from './odeon-cartridge/odeon-cartridge.component';

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
    AppMaterialModule,
    environment.production ? [] : HttpClientInMemoryWebApiModule.forRoot(InMemoryAudioService, {
      apiBase: "api/",
      delay: 300,
    }),
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
