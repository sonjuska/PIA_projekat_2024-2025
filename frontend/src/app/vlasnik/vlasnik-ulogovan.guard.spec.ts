import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { vlasnikUlogovanGuard } from './vlasnik-ulogovan.guard';

describe('vlasnikUlogovanGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => vlasnikUlogovanGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
