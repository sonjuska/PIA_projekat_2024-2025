import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ChartData } from '../../responses/KolonaDijagramPodaci';
import { PieData } from '../../responses/PitaDijagramPodaci';


@Injectable({
  providedIn: 'root'
})
export class VlasnikStatistikaService {

  constructor(private http: HttpClient ) {}
  private url = 'http://localhost:8080/vlasnik-statistika';

  dohvatiPodatkeZaDijagramKolona(korisnicko_ime: string){
    return this.http.get<ChartData[]>(`${this.url}/dijagramKolona`, {params: {korisnicko_ime: korisnicko_ime}})
  }
  dohvatiPodatkeDijagramPita(korisnicko_ime: string){
    return this.http.get<PieData[]>(`${this.url}/dijagramPita`, {params: {korisnicko_ime: korisnicko_ime}})
  }
}
