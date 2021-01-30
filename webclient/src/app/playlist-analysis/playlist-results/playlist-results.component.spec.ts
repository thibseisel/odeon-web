import { async, ComponentFixture, TestBed } from '@angular/core/testing'

import { PlaylistResultsComponent } from './playlist-results.component'

describe('PlaylistResultsComponent', () => {
  let component: PlaylistResultsComponent
  let fixture: ComponentFixture<PlaylistResultsComponent>

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PlaylistResultsComponent ]
    })
    .compileComponents()
  }))

  beforeEach(() => {
    fixture = TestBed.createComponent(PlaylistResultsComponent)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })
})
