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
  azurirajKorisnika(korisnik: KorisnikLoginResponse): Observable<SimpleResponse>{
    return this.http.put<SimpleResponse>(`${this.url}/azurirajKorisnika`, korisnik)
  }
  deaktivirajKorisnika(korisnicko_ime: string): Observable<SimpleResponse>{
    return this.http.get<SimpleResponse>(`${this.url}/deaktivirajKorisnika`, {params: {korisnicko_ime: korisnicko_ime}})
  }
  dohvatiVikendice(): Observable<Vikendica[]>{
    return this.http.get<Vikendica[]>(this.url+'/vikendice')
  }
}
