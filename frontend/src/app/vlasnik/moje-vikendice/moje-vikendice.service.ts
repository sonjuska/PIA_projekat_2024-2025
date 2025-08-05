import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Vikendica } from '../../models/vikendica';
import { VikendicaSimpleResponse} from '../../responses/VikendicaSimpleResponse';
import { NovaVikendicaRequest } from '../../requests/NovaVIkendicaRequest';

@Injectable({
  providedIn: 'root'
})
export class MojeVikendiceService {

  private url = 'http://localhost:8080/vikendice'
  constructor(private http: HttpClient) { }

  getMojeVikendice(vlasnik: string): Observable<Vikendica[]> {
    return this.http.get<Vikendica[]>(`${this.url}/moje-vikendice`, {params: {vlasnik: vlasnik}});
  }
  getVikendicaPoId(id: number): Observable<Vikendica>{
    return this.http.get<Vikendica>(`${this.url}/id`, {params: {id: id}})
  }
  obrisiVikendicu(id: number): Observable<VikendicaSimpleResponse> {
    return this.http.delete<VikendicaSimpleResponse>(`${this.url}/moje-vikendice/${id}`);
  }
  azurirajVikendicu(vik: Vikendica, slike: string[], obrisaneSlike: string[]): Observable<VikendicaSimpleResponse>{
    let body = {
      vikendica: vik,          
      noveSlike: slike, 
      obrisaneSlike: obrisaneSlike 
    };
    return this.http.put<VikendicaSimpleResponse>(`${this.url}/azuriraj`, body );
  }
  dodajVikendicuSaCenovnikom(vik: NovaVikendicaRequest): Observable<VikendicaSimpleResponse> {
    return this.http.post<VikendicaSimpleResponse>(`${this.url}/dodajSaCenovnikom`, vik);
  }
  getSlikeVikendice(vikendica_id: number): Observable<string[]>{
    return this.http.get<string[]>(`${this.url}/id/slike`, {params: {vikendica_id: vikendica_id}})
  }


}
