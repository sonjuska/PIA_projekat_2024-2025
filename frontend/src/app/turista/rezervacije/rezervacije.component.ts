import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { VikendicaService } from '../vikendice/vikendica.service';
import { KorisnikLoginResponse } from '../../responses/KorisnikLoginResponse';
import { TuristaService } from '../turista.service';
import { DohvatiRezervacijuResponse } from '../../responses/DohvatiRezervacijuResponse';
import { RezervacijeService } from './rezervacije.service';
import { ArhivaRezervacijaResponse } from '../../responses/arhivaRezervacijaResponse';

@Component({
  selector: 'app-rezervacije',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './rezervacije.component.html',
  styleUrl: './rezervacije.component.css'
})
export class RezervacijeComponent implements OnInit{
  ngOnInit(): void {
    let k = localStorage.getItem('korisnik');
    if(k){
      let korisnik = JSON.parse(k);
      this.turistaServis.dohvatiKorisnika(korisnik.korisnicko_ime).subscribe(kor=>{
        if(kor){
          this.korisnik = kor;
          this.rezervacijaServis.dohvatiAktivneRezervacije(this.korisnik.korisnicko_ime).subscribe(rez=>{
            this.aktivneRezervacije = rez;
          })
            this.rezervacijaServis.dohvatiArhiviraneRezervacije(this.korisnik.korisnicko_ime).subscribe(rez=>{
            this.arhiviraneRezervacije = rez;
            console.log(this.arhiviraneRezervacije)
          })
        }
      })
    }
  }

    vikendicaServis = inject(VikendicaService)
    turistaServis = inject(TuristaService)
    rezervacijaServis = inject(RezervacijeService)
    ruta = inject(ActivatedRoute)
  
    korisnik: KorisnikLoginResponse = new KorisnikLoginResponse();
    aktivneRezervacije: DohvatiRezervacijuResponse[] = [];
    arhiviraneRezervacije: ArhivaRezervacijaResponse[] = []
    novaOcena: number = 0;
    noviTekst: string = '';
    openedIndex: number | null = null;
    tekst: string = '';
    ocena: number = 0;

    otvoriKomentar(i: number): void {
      if(this.openedIndex===i) this.openedIndex = null;
      else{
        this.openedIndex = i;
        this.tekst = '';
      }

    }
  posaljiKomentar(arg0: any) {
    throw new Error('Method not implemented.');
  }
}
