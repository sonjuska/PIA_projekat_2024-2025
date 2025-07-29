import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Vikendica } from '../../models/vikendica';
import { Slika } from '../../models/slika';
import { Cenovnik } from '../../models/cenovnik';
import { Komentar } from '../../models/komentar';

@Injectable({
  providedIn: 'root'
})
export class VikendicaService {

  private url = 'http://localhost:8080/vikendice';

  constructor(private http: HttpClient) { }

  //dohvata i prosecne ocene za vikendice
  getVikendice(): Observable<Vikendica[]> {
    return this.http.get<Vikendica[]>(`${this.url}/sve`);
  }
  getVikendicaPoId(id: number): Observable<Vikendica>{
    return this.http.get<Vikendica>(`${this.url}/id`, {params: {id: id}})
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

  getSlikeVikendice(vikendica_id: number): Observable<string[]>{
    return this.http.get<string[]>(`${this.url}/id/slike`, {params: {vikendica_id: vikendica_id}})
  }

  getCenovnikVikendice(vikendica_id: number): Observable<Cenovnik[]>{
    return this.http.get<Cenovnik[]>(`${this.url}/id/cenovnik`, {params: {vikendica_id: vikendica_id}})
  }
  getKomentareVikendice(vikendica_id: number): Observable<Komentar[]>{
    return this.http.get<Komentar[]>(`${this.url}/id/komentari`, {params: {vikendica_id: vikendica_id}})
  }
}
