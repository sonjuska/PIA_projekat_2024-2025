export class Korisnik {
  korisnicko_ime: string = '';
  lozinka_hash: string = '';
  ime: string = '';
  prezime: string = '';
  pol: 'M' | 'Z' = 'M';
  adresa: string = '';
  telefon: string = '';
  email: string = '';
  profilna_slika_path: string = '';
  broj_kartice: string = '';
  uloga: string = '';
  aktivan: boolean = false;
}
