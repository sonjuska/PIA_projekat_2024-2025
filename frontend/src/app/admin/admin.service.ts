import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ZahtevZaRegistraciju } from '../models/zahtevZaRegistraciju';

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  constructor(private http: HttpClient) { }

  private url = 'http://localhost:8080/admin';

  sviZahteviZaRegistraciju(): Observable<ZahtevZaRegistraciju[]>{
    return this.http.get<ZahtevZaRegistraciju[]>(this.url+'/zahtevi')
  }
  odobri(id: number){
    return this.http.get<number>(this.url + '/odobriZahtev', {params:{id:id}})
  }
  odbij(id: number, komentar: string){
    return this.http.get<number>(this.url + '/odbijZahtev', {params: {id:id, komentar_odbijanja: komentar}})
  }
}
