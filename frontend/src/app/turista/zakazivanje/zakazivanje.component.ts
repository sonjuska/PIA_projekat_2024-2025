import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { Vikendica } from '../../models/vikendica';
import { VikendicaService } from '../vikendice/vikendica.service';
import { KorisnikLoginResponse } from '../../responses/KorisnikLoginResponse';
import { TuristaService } from '../turista.service';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatNativeDateModule } from '@angular/material/core';
import { ZakazivanjeService } from './zakazivanje.service';


@Component({
  selector: 'app-zakazivanje',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule,
    FormsModule,
    MatDatepickerModule,
    MatFormFieldModule,
    MatInputModule,
    MatNativeDateModule],
  templateUrl: './zakazivanje.component.html',
  styleUrl: './zakazivanje.component.css'
})
export class ZakazivanjeComponent implements OnInit{
  sutra: string = new Date(Date.now() + 24 * 60 * 60 * 1000).toISOString().split('T')[0];

  ngOnInit(): void {

    const id = this.ruta.snapshot.paramMap.get('id');
    if (id) {
      this.vikendicaServis.getVikendicaPoId(+id).subscribe(vik => {
        if (vik) {
          this.vikendica = vik;
        }
      });
    }

    let k = localStorage.getItem('korisnik');
    if(k){
      let korisnik = JSON.parse(k);
      this.turistaServis.dohvatiKorisnika(korisnik.korisnicko_ime).subscribe(kor=>{
        if(kor){
          this.korisnik = kor;
          this.brojKartice = this.korisnik.broj_kartice;
        }
      })
    }
    this.satiDolaska = this.generisiSate(14, 23);
    this.satiOdlaska = this.generisiSate(5, 10);

  }

  vikendicaServis = inject(VikendicaService)
  turistaServis = inject(TuristaService)
  zakazivanjeServis = inject(ZakazivanjeService)
  ruta = inject(ActivatedRoute)

  korisnik: KorisnikLoginResponse = new KorisnikLoginResponse();
  vikendica: Vikendica = new Vikendica();
  korak: number = 1;
  datumOd: string = '';
  datumDo: string = '';
  satiDolaska: string[] = [];
  satiOdlaska: string[] = [];
  vremeOd: string = '14:00';
  vremeDo: string = '10:00';
  brojOdraslih: number = 1;
  brojDece: number = 0;
  brojKartice: string = '';
  ukupnaCena: number = 0.0;
  dodatniZahtevi: string = ''

  porukaGreske: string = '';

  sledeciKorak(){
    if(new Date(this.datumOd)>=new Date(this.datumDo)){
      this.porukaGreske = 'Datum dolaska mora biti pre datuma odlaska.';
      return;
    }
    if(this.korak==1){
      this.korak = 2;
      this.porukaGreske = '';
      this.zakazivanjeServis.izracunajCenuZakazivanja(this.vikendica.id, this.datumOd, this.datumDo, this.brojOdraslih, this.brojDece).subscribe(cena=>{
        this.ukupnaCena = cena;
      });
    }
  }
  prethodniKorak(){
    if(this.korak==2) this.korak = 1;
  }
  potvrdiZakazivanje(){
    this.zakazivanjeServis.potvrdiZakazivanje(this.vikendica.id, this.korisnik.korisnicko_ime, this.datumOd, this.vremeOd, this.datumDo,
      this.vremeDo, this.brojOdraslih, this.brojDece, this.brojKartice, this.dodatniZahtevi
    ).subscribe(res=>{
      alert(res.poruka)
      this.korak = 1;
    })
  }

  generisiSate(pocetak: number, kraj: number): string[] {
    let rezultati: string[] = [];
    for (let sat = pocetak; sat <= kraj; sat++) {
      rezultati.push((sat < 10 ? '0' : '') + sat + ':00');
    }
    return rezultati;
  }

  dozvoliSamoBrojeve(event: KeyboardEvent): void {
    const charCode = event.key.charCodeAt(0);
    if (charCode < 48 || charCode > 57) {
      event.preventDefault();
    }
  }

}
