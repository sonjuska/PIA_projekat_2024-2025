import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ZakazivanjeService {

  constructor(private http: HttpClient) { }
  private url = 'http://localhost:8080/zakazivanje'

  izracunajCenuZakazivanja(vikendica_id: number, datumOd: string, datumDo: string, brojOdraslih: number, brojDece: number){
    return this.http.get<number>(`${this.url}/izracunajCenu`, {params:
      {
        vikendica_id: vikendica_id,
        datumOd: datumOd,
        datumDo: datumDo,
        brojOdraslih: brojOdraslih,
        brojDece: brojDece
      }
    })
  }
}
