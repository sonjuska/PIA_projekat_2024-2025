import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { KorisnikLoginResponse } from '../responses/KorisnikLoginResponse';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  private url = 'http://localhost:8080/login';

    constructor(private http: HttpClient) {}

    loginKorisnik(korime: string, lozinka: string): Observable<KorisnikLoginResponse> {
      const body = { korisnicko_ime: korime, lozinka_hash: lozinka };
      return this.http.post<KorisnikLoginResponse>(this.url, body);
    }
}
