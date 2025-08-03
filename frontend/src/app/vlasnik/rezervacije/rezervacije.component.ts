import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { VlasnikService } from '../vlasnik.service';
import { DohvatiRezervacijuResponse } from '../../responses/DohvatiRezervacijuResponse';
import { KorisnikLoginResponse } from '../../responses/KorisnikLoginResponse';
import { RezervacijeService } from './rezervacije.service';


@Component({
  selector: 'app-rezervacije',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './rezervacije.component.html',
  styleUrl: './rezervacije.component.css'
})
export class RezervacijeComponent implements OnInit{
  ngOnInit(): void {
    let k = localStorage.getItem('korisnik');
    if(k){
      let korisnik = JSON.parse(k);
      this.vlasnikServis.dohvatiKorisnika(korisnik.korisnicko_ime).subscribe(kor=>{
        if(kor){
          this.korisnik = kor;
          this.rezervacijaServis.dohvatiMojeRezervacije(this.korisnik.korisnicko_ime).subscribe(rez=>{
            this.rezervacije = rez;
          })
        }
      })
    }
  }

  vlasnikServis = inject(VlasnikService)
  rezervacijaServis = inject(RezervacijeService)
  ruta = inject(ActivatedRoute)

  korisnik: KorisnikLoginResponse = new KorisnikLoginResponse();
  rezervacije: DohvatiRezervacijuResponse[] = [];
}
