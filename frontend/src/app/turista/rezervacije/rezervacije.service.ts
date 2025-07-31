import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { DohvatiRezervacijuResponse } from '../../responses/DohvatiRezervacijuResponse';
import { Observable } from 'rxjs';
import { ArhivaRezervacijaResponse } from '../../responses/arhivaRezervacijaResponse';

@Injectable({
  providedIn: 'root'
})
export class RezervacijeService {

  constructor(private http: HttpClient) { }

  private url = 'http://localhost:8080/turista/rezervacije';

  dohvatiAktivneRezervacije(turista: string): Observable<DohvatiRezervacijuResponse[]>{
    return this.http.get<DohvatiRezervacijuResponse[]>(`${this.url}/aktivne`, {params: {turista: turista}});
  }
  dohvatiArhiviraneRezervacije(turista: string): Observable<ArhivaRezervacijaResponse[]>{
    return this.http.get<ArhivaRezervacijaResponse[]>(`${this.url}/arhivirane`, {params: {turista: turista}});
  }
}
