import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { adminUlogovanGuard } from './admin-ulogovan.guard';

describe('adminUlogovanGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => adminUlogovanGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
