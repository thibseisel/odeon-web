import { HttpClientModule } from "@angular/common/http";
import { NgModule, ModuleWithProviders } from "@angular/core";
import { BrowserModule } from "@angular/platform-browser";
import { HttpClientInMemoryWebApiModule } from "angular-in-memory-web-api";
import { environment } from "src/environments/environment";
import { AppMaterialModule } from "./app-material.module";
import { AppRoutingModule } from "./app-routing.module";
import { AppComponent } from "./app.component";
import { InMemoryAudioService } from "./audio.inmemoryservice";
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
    AppMaterialModule,
    (environment.useMockServer) ? configureMockServer() : []
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }

function configureMockServer(): ModuleWithProviders<HttpClientInMemoryWebApiModule> {
  return HttpClientInMemoryWebApiModule.forRoot(InMemoryAudioService, {
    apiBase: "api/",
    delay: 300
  });
}
