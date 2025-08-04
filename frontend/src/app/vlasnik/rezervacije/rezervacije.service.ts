import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { DohvatiRezervacijuResponse } from '../../responses/DohvatiRezervacijuResponse';
import { RezervacijaResponse } from '../../responses/RezervacijaResponse';

@Injectable({
  providedIn: 'root'
})
export class RezervacijeService {
  constructor(private http: HttpClient) { }

  private url = 'http://localhost:8080/vlasnik/rezervacije';

  dohvatiMojeRezervacije(vlasnik: string): Observable<DohvatiRezervacijuResponse[]>{
    return this.http.get<DohvatiRezervacijuResponse[]>(`${this.url}/moje-rezervacije`, {params: {vlasnik: vlasnik}});
  }
  potvrdiRezervaciju(id: number){
    return this.http.put<RezervacijaResponse>(`${this.url}/potvrdiRezervaciju/${id}`, {});
  }
  odbijRezervaciju(id: number, komentar: string){
    return this.http.put<RezervacijaResponse>(`${this.url}/odbijRezervaciju`, {id: id, komentar_odbijanja: komentar});
  }

}
