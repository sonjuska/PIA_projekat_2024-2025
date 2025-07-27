import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const adminUlogovanGuard: CanActivateFn = (route, state) => {
  const admin = localStorage.getItem('admin');

  if (admin) {
    return true;
  } else {
    const ruter = inject(Router);
    ruter.navigate(['/admin/prijava']);
    return false;
  }
};
