import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { KorisnikLoginResponse } from '../../responses/KorisnikLoginResponse';

@Injectable({
  providedIn: 'root'
})
export class AdminLoginService {

  constructor(private http: HttpClient) { }

  private url = 'http://localhost:8080/adminLogin';

  loginAdmin(korime: string, lozinka: string): Observable<KorisnikLoginResponse> {
    const body = { korisnicko_ime: korime, lozinka_hash: lozinka };
    return this.http.post<KorisnikLoginResponse>(this.url, body);
  }
}
