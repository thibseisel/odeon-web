import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing'

import { OdeonCartridgeComponent } from './odeon-cartridge.component'

describe('OdeonCartridgeComponent', () => {
  let component: OdeonCartridgeComponent
  let fixture: ComponentFixture<OdeonCartridgeComponent>

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ OdeonCartridgeComponent ]
    })
    .compileComponents()
  }))

  beforeEach(() => {
    fixture = TestBed.createComponent(OdeonCartridgeComponent)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })
})
