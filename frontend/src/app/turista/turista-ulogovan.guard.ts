import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const turistaUlogovanGuard: CanActivateFn = (route, state) => {
  const korisnik = localStorage.getItem('korisnik');

  if (korisnik) {
    let k = JSON.parse(korisnik)
    return k.uloga=='turista';
  } else {
    const ruter = inject(Router);
    ruter.navigate(['/prijava']);
    return false;
  }
};
