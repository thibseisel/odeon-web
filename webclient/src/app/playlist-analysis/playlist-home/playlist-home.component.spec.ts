import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing'

import { PlaylistHomeComponent } from './playlist-home.component'

describe('PlaylistHomeComponent', () => {
  let component: PlaylistHomeComponent
  let fixture: ComponentFixture<PlaylistHomeComponent>

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ PlaylistHomeComponent ]
    })
    .compileComponents()
  }))

  beforeEach(() => {
    fixture = TestBed.createComponent(PlaylistHomeComponent)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })
})
