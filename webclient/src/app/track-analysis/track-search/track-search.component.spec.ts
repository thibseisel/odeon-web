import { ComponentFixture, TestBed, waitForAsync } from "@angular/core/testing"

import { TrackSearchComponent } from "./track-search.component"

describe("TrackSearchComponent", () => {
  let component: TrackSearchComponent
  let fixture: ComponentFixture<TrackSearchComponent>

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ TrackSearchComponent ]
    })
      .compileComponents()
  }))

  beforeEach(() => {
    fixture = TestBed.createComponent(TrackSearchComponent)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it("should create", () => {
    expect(component).toBeTruthy()
  })
})
