import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PromenaLozinkeResponse } from '../responses/PromenaLozinkeResponse';

@Injectable({
  providedIn: 'root'
})
export class PromenaLozinkeService {


  private url = 'http://localhost:8080/promeniLozinku'; 
  
  constructor(private http: HttpClient) {}

  promeniLozinku(korisnicko_ime: string, staraLozinka: string, novaLozinka: string): Observable<PromenaLozinkeResponse> {
    const body = {
      korisnicko_ime: korisnicko_ime,
      staraLozinka: staraLozinka,
      novaLozinka: novaLozinka};
    return this.http.post<PromenaLozinkeResponse>(this.url, body);
  }
}
