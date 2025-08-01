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

            //inicijalizacija mape za komentarisanje arhiviranih rez
            this.arhiviraneRezervacije.forEach((r, index) => {
              if ((r.ocena === 0 || r.ocena == null) && (!r.tekst || r.tekst.trim() === '')) {
                this.mapaOtvorenihKomentara.set(index, false);
              }
            });

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
  porukaGreske: string = '';

  mapaOtvorenihKomentara: Map<number, boolean> = new Map();


  otvoriKomentar(i: number): void {
    const trenutno = this.mapaOtvorenihKomentara.get(i) || false;
    this.mapaOtvorenihKomentara.set(i, !trenutno);
  }

  posaljiKomentar(a: ArhivaRezervacijaResponse) {
    if(!a.tekst || a.tekst.trim() === '' || !a.ocena){
      this.porukaGreske = 'Morate uneti i komentar i ocenu.';
      return;
    }
    this.rezervacijaServis.posaljiKomentar(a).subscribe(res => {
      if (res) {
        this.porukaGreske = '';
        this.rezervacijaServis.dohvatiArhiviraneRezervacije(this.korisnik.korisnicko_ime).subscribe(rez => {
          this.arhiviraneRezervacije = rez;

          //reeinicijalizovanje mape komentara
          this.mapaOtvorenihKomentara.clear();
          this.arhiviraneRezervacije.forEach((r, index) => {
            if ((r.ocena === 0 || r.ocena == null) && (!r.tekst || r.tekst.trim() === '')) {
              this.mapaOtvorenihKomentara.set(index, false);
            }
          });
        });
      }
    });
  }

}
