import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class StatistikaService {

  private url = 'http://localhost:8080/statistika'; 

  constructor(private http: HttpClient) {}

  getUkupanBrojVikendica(): Observable<number> {
    return this.http.get<number>(`${this.url}/ukupanBrojVikendica`);
  }

  getUkupanBrojVlasnika(): Observable<number> {
    return this.http.get<number>(`${this.url}/ukupanBrojVlasnika`);
  }

  getUkupanBrojTurista(): Observable<number> {
    return this.http.get<number>(`${this.url}/ukupanBrojTurista`);
  }

  getBrojRezervacija24h(): Observable<number> {
    return this.http.get<number>(`${this.url}/rezervacije24h`);
  }

  getBrojRezervacija7dana(): Observable<number> {
    return this.http.get<number>(`${this.url}/rezervacije7dana`);
  }

  getBrojRezervacija30dana(): Observable<number> {
    return this.http.get<number>(`${this.url}/rezervacije30dana`);
  }
}
