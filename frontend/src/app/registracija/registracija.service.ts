import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { RegistracijaResponse } from '../responses/RegistracijaResponse';

@Injectable({
  providedIn: 'root'
})
export class RegistracijaService {

  private url = 'http://localhost:8080/registracija'; 

  constructor(private http: HttpClient) {}

    registruj(korime: string, lozinka: string, ime: string, prezime: string, pol: string, adresa: string,
      telefon: string, email: string, brojKartice: string, uloga: string, slika: File | null
    ): Observable<RegistracijaResponse> {

      const formData = new FormData();
      formData.append('korime', korime);
      formData.append('lozinka', lozinka);
      formData.append('ime', ime);
      formData.append('prezime', prezime);
      formData.append('pol', pol);
      formData.append('adresa', adresa);
      formData.append('telefon', telefon);
      formData.append('email', email);
      formData.append('brojKartice', brojKartice);
      formData.append('uloga', uloga);

      if (slika) {
        formData.append('slika', slika, slika.name);
      }
      
      return this.http.post<RegistracijaResponse>(this.url, formData);
    }
}
