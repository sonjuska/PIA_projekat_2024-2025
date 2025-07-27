import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { turistaUlogovanGuard } from './turista-ulogovan.guard';

describe('turistaUlogovanGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => turistaUlogovanGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
