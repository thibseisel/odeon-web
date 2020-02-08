import { HttpClientModule } from "@angular/common/http";
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientInMemoryWebApiModule } from "angular-in-memory-web-api";
import { environment } from 'src/environments/environment';
import { AppMaterialModule } from './app-material.module';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { InMemoryAudioService } from './audio.inmemory.service';
import { TrackDetailComponent } from './track-detail/track-detail.component';
import { TrackMetadataComponent } from './track-metadata/track-metadata.component';
import { TrackSearchComponent } from './track-search/track-search.component';

@NgModule({
  declarations: [
    AppComponent,
    TrackMetadataComponent,
    TrackSearchComponent,
    TrackDetailComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    AppMaterialModule,
    environment.production ? [] : HttpClientInMemoryWebApiModule.forRoot(InMemoryAudioService, {
      apiBase: 'api/',
      delay: 300,
    }),
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
