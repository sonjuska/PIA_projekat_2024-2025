import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Vikendica } from './models/vikendica';

@Injectable({
  providedIn: 'root'
})
export class VikendicaService {

  private url = 'http://localhost:8080/vikendice';

  constructor(private http: HttpClient) { }

  getVikendice(): Observable<Vikendica[]> {
    return this.http.get<Vikendica[]>(`${this.url}/sve`);
  }

  pretraziVikendice(naziv: string, mesto: string): Observable<Vikendica[]> {
    let params = new HttpParams();
    if (naziv) {
      params = params.set('naziv', naziv);
    }
    if (mesto) {
      params = params.set('mesto', mesto);
    }
    return this.http.get<Vikendica[]>(`${this.url}/pretraga`, { params });
  }
}
