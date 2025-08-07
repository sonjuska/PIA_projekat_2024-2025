import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ZahtevZaRegistraciju } from '../models/zahtevZaRegistraciju';
import { KorisnikLoginResponse } from '../responses/KorisnikLoginResponse';
import { Vikendica } from '../models/vikendica';
import { SimpleResponse } from '../responses/SimpleResponse';

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  constructor(private http: HttpClient) { }

  private url = 'http://localhost:8080/admin';

  sviZahteviZaRegistraciju(): Observable<ZahtevZaRegistraciju[]>{
    return this.http.get<ZahtevZaRegistraciju[]>(this.url+'/zahtevi')
  }
  odobri(id: number){
    return this.http.get<number>(this.url + '/odobriZahtev', {params:{id:id}})
  }
  odbij(id: number, komentar: string){
    return this.http.get<number>(this.url + '/odbijZahtev', {params: {id:id, komentar_odbijanja: komentar}})
  }
  dohvatiKorisnike(): Observable<KorisnikLoginResponse[]>{
    return this.http.get<KorisnikLoginResponse[]>(this.url+'/korisnici')
  }
  dohvatiKorisnikaPoKorisnickomImenu(korisnicko_ime: string):Observable<KorisnikLoginResponse>{
    return this.http.get<KorisnikLoginResponse>(this.url+'/uredi-korisnika/korisnik', {params: {korisnicko_ime: korisnicko_ime}})
  }
  azurirajKorisnika(korisnik: KorisnikLoginResponse, novaSlika: File | null, slikaUklonjena: boolean): Observable<SimpleResponse> {
    const formData = new FormData();

    formData.append('korisnicko_ime', korisnik.korisnicko_ime);
    formData.append('ime', korisnik.ime);
    formData.append('prezime', korisnik.prezime);
    formData.append('pol', korisnik.pol);
    formData.append('adresa', korisnik.adresa);
    formData.append('telefon', korisnik.telefon);
    formData.append('email', korisnik.email);
    formData.append('broj_kartice', korisnik.broj_kartice);
    formData.append('uloga', korisnik.uloga);
    formData.append('aktivan', korisnik.aktivan.toString());
    formData.append('slikaUklonjena', slikaUklonjena.toString());

    if (novaSlika) {
      formData.append('slika', novaSlika);
    }

    return this.http.post<SimpleResponse>(`${this.url}/azurirajKorisnika`, formData)
  }
  deaktivirajKorisnika(korisnicko_ime: string): Observable<SimpleResponse>{
    return this.http.get<SimpleResponse>(`${this.url}/deaktivirajKorisnika`, {params: {korisnicko_ime: korisnicko_ime}})
  }
  dohvatiVikendice(): Observable<Vikendica[]>{
    return this.http.get<Vikendica[]>(this.url+'/vikendice')
  }
}
