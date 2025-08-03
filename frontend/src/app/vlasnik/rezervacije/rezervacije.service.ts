import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ArhivaRezervacijaResponse } from '../../responses/arhivaRezervacijaResponse';
import { DohvatiRezervacijuResponse } from '../../responses/DohvatiRezervacijuResponse';

@Injectable({
  providedIn: 'root'
})
export class RezervacijeService {
  constructor(private http: HttpClient) { }

  private url = 'http://localhost:8080/vlasnik/rezervacije';

  dohvatiMojeRezervacije(vlasnik: string): Observable<DohvatiRezervacijuResponse[]>{
    return this.http.get<DohvatiRezervacijuResponse[]>(`${this.url}/moje-rezervacije`, {params: {vlasnik: vlasnik}});
  }

}
