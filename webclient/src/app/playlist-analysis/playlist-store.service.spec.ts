import { TestBed } from '@angular/core/testing'

import { PlaylistStoreService } from './playlist-store.service'

describe('PlaylistStoreService', () => {
  let service: PlaylistStoreService

  beforeEach(() => {
    TestBed.configureTestingModule({})
    service = TestBed.inject(PlaylistStoreService)
  })

  it('should be created', () => {
    expect(service).toBeTruthy()
  })
})
