import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { RezervacijaResponse } from '../../responses/RezervacijaResponse';

@Injectable({
  providedIn: 'root'
})
export class ZakazivanjeService {

  constructor(private http: HttpClient) { }
  private url = 'http://localhost:8080/zakazivanje'

  izracunajCenuZakazivanja(vikendica_id: number, datumOd: string, datumDo: string, brojOdraslih: number, brojDece: number):Observable<number>{
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
  potvrdiZakazivanje(vikendica_id: number, turista: string, datum_od: string, vreme_od: string, datum_do: string,
    vreme_do: string, broj_odraslih: number, broj_dece: number, broj_kartice: string, opis: string):Observable<RezervacijaResponse>{
      const body = {
        vikendica_id: vikendica_id,
        turista: turista,
        datum_od: datum_od,
        vreme_od: vreme_od,
        datum_do: datum_do,
        vreme_do: vreme_do,
        broj_odraslih: broj_odraslih,
        broj_dece: broj_dece,
        broj_kartice: broj_kartice,
        opis: opis
      }
      console.log(body);
      return this.http.post<RezervacijaResponse>(`${this.url}/potvrdiZakazivanje`, body)
    }
}
