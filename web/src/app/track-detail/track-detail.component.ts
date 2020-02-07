import { Component } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { Observable } from 'rxjs';
import { concatMap } from 'rxjs/operators';
import { mapIfDefined as mapToDefined } from '../rx-operators';
import { TrackMetadataService } from '../track-metadata.service';
import { Track } from '../track-models';

@Component({
  selector: 'app-track-detail',
  templateUrl: './track-detail.component.html',
  styles: []
})
export class TrackDetailComponent {

  public asyncTrack: Observable<Track> = this.routes.paramMap.pipe(
    mapToDefined((urlParams: ParamMap) => urlParams.get('id')),
    concatMap((trackId) => this.source.getTrackMetadata(trackId))
  );

  constructor(
    private source: TrackMetadataService,
    private routes: ActivatedRoute
  ) { }
}