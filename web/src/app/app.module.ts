import { BrowserModule } from '@angular/platform-browser';
import { NgModule, ModuleWithProviders } from '@angular/core';
import { HttpClientModule } from "@angular/common/http";
import { HttpClientInMemoryWebApiModule } from "angular-in-memory-web-api";

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { TrackSearchComponent } from './track-search/track-search.component';
import { environment } from 'src/environments/environment';
import { InMemoryAudioService } from './audio.inmemory.service';
import { TrackDetailComponent } from './track-detail/track-detail.component';

@NgModule({
  declarations: [
    AppComponent,
    TrackSearchComponent,
    TrackDetailComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    environment.production ? [] : HttpClientInMemoryWebApiModule.forRoot(InMemoryAudioService, {
      apiBase: 'api/',
      delay: 300,
    })
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
