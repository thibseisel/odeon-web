import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from "@angular/common/http";
import { HttpClientInMemoryWebApiModule } from "angular-in-memory-web-api";

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { TrackSearchComponent } from './track-search/track-search.component';
import { environment } from 'src/environments/environment';
import { InMemoryService } from './InMemoryService';

@NgModule({
  declarations: [
    AppComponent,
    TrackSearchComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    environment.production ? [] : HttpClientInMemoryWebApiModule.forRoot(InMemoryService)
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
