import { KorisnikLoginResponse } from './../responses/KorisnikLoginResponse';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TuristaService {

  constructor(private http: HttpClient) { }
  private url = 'http://localhost:8080/turista'

  dohvatiKorisnika(korisnicko_ime: string): Observable<KorisnikLoginResponse>{
    return this.http.get<KorisnikLoginResponse>(this.url + '/dohvatiKorisnika', {params: {korisnicko_ime: korisnicko_ime}});
  }

  azurirajKorisnika(korisnik: KorisnikLoginResponse, novaSlika: File | null, slikaUklonjena: boolean): Observable<boolean> {
    const formData = new FormData();

    formData.append('korisnicko_ime', korisnik.korisnicko_ime);
    formData.append('ime', korisnik.ime);
    formData.append('prezime', korisnik.prezime);
    formData.append('adresa', korisnik.adresa);
    formData.append('telefon', korisnik.telefon);
    formData.append('email', korisnik.email);
    formData.append('broj_kartice', korisnik.broj_kartice);
    formData.append('slikaUklonjena', slikaUklonjena.toString());

    if (novaSlika) {
      formData.append('slika', novaSlika);
    }

    return this.http.post<boolean>(this.url + '/azuriraj', formData);
  }


}
