import { TestBed } from '@angular/core/testing';

import { VlasnikStatistikaService } from './vlasnik-statistika.service';

describe('VlasnikStatistikaService', () => {
  let service: VlasnikStatistikaService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VlasnikStatistikaService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
