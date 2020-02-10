import { async, ComponentFixture, TestBed } from "@angular/core/testing";

import { TrackMetadataComponent } from "./track-metadata.component";

describe("TrackMetadataComponent", () => {
  let component: TrackMetadataComponent;
  let fixture: ComponentFixture<TrackMetadataComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TrackMetadataComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TrackMetadataComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it("should create", () => {
    expect(component).toBeTruthy();
  });
});
