import { TestBed } from "@angular/core/testing";

import { TrackMetadataService } from "./track-metadata.service";

describe("TrackMetadataService", () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it("should be created", () => {
    const service: TrackMetadataService = TestBed.inject(TrackMetadataService);
    expect(service).toBeTruthy();
  });
});
