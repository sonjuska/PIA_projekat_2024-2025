import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { Vikendica } from '../../../models/vikendica';
import { VikendicaService } from '../vikendica.service';
import { CommonModule} from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Cenovnik } from '../../../models/cenovnik';
import * as L from 'leaflet';
import { Komentar } from '../../../models/komentar';

@Component({
  selector: 'app-vikendica-detalji',
  standalone: true,
  imports: [FormsModule, CommonModule, RouterModule],
  templateUrl: './vikendica-detalji.component.html',
  styleUrl: './vikendica-detalji.component.css'
})
export class VikendicaDetaljiComponent implements OnInit{
  ngOnInit(): void {
    const id = this.ruta.snapshot.paramMap.get('id');
    if (id) {
      this.vikendicaServis.getVikendicaPoId(+id).subscribe(vik => {
        if (vik) {
          this.vikendica = vik;
          this.usluge = this.vikendica.usluge ? this.vikendica.usluge.split(',').map(u => u.trim()) : [];

          this.vikendicaServis.getSlikeVikendice(this.vikendica.id).subscribe(slike => {
            this.slike = slike;
            console.log(this.slike)
          });
          this.vikendicaServis.getCenovnikVikendice(this.vikendica.id).subscribe(cenovnik=>{
            this.cenovnik = cenovnik;
          })
          this.vikendicaServis.getKomentareVikendice(this.vikendica.id).subscribe(komentari=>{
            this.komentari = komentari;
          })
        }
      });
    }

    //mapa
    setTimeout(() => {
      const customIcon = L.icon({
        iconUrl: '/pin_ikona.png',
        iconSize: [20, 50],     //sirina, visina u pikselima
        iconAnchor: [10, 50],           //donji centar ikone
        popupAnchor: [0, -40]          //popup ide iznad ikonice
      });
      if (this.vikendica) {
        const map = L.map('map').setView([this.vikendica.lat, this.vikendica.lon], 13);
        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
          attribution: '© OpenStreetMap contributors'
        }).addTo(map);
        L.marker([this.vikendica.lat, this.vikendica.lon], {icon: customIcon}).addTo(map)
      }
    }, 100); //ceka da se DOM inicijalizuje
    document.addEventListener('keydown', this.osluskujTastaturu.bind(this));    //za menjanje slika na dugmice
  }

  ruta = inject(ActivatedRoute);
  ruter = inject(Router)
  vikendicaServis = inject(VikendicaService);

  vikendica: Vikendica = new Vikendica();
  usluge: string[] = [];
  slike: string[] = [];
  cenovnik: Cenovnik[] = [];
  komentari: Komentar[] = []

  //paginacija
  trenutnaStranica: number = 1;
  brojPoStrani: number = 3;

  //modal
  prikazanaSlika: string | null = null;

  otvoriModal(slika: string): void {
    this.prikazanaSlika = slika;
  }

  zatvoriModal(): void {
    this.prikazanaSlika = null;
  }

  osluskujTastaturu(event: KeyboardEvent): void {
    if (!this.prikazanaSlika) return;

    if (event.key === 'ArrowRight') {
      this.prikaziSledecuSliku();
    }
    if (event.key === 'ArrowLeft') {
      this.prikaziPrethodnuSliku();
    }
    if (event.key === 'Escape') {
      this.zatvoriModal();
    }
  }

  prikaziSledecuSliku(): void {
    if (!this.prikazanaSlika || this.slike.length === 0) return;

    const trenutniIndex = this.slike.indexOf(this.prikazanaSlika);
    const sledeciIndex = (trenutniIndex + 1) % this.slike.length;
    this.prikazanaSlika = this.slike[sledeciIndex];
  }

  prikaziPrethodnuSliku(): void {
    if (!this.prikazanaSlika || this.slike.length === 0) return;

    const trenutniIndex = this.slike.indexOf(this.prikazanaSlika);
    const prethodniIndex = (trenutniIndex-1 + this.slike.length) % this.slike.length;
    this.prikazanaSlika = this.slike[prethodniIndex];
  }


  get prikazaniKomentari(): any[] {
    const start = (this.trenutnaStranica - 1) * this.brojPoStrani;
    return this.komentari.slice(start, start + this.brojPoStrani);
  }

  ukupnoStranica(): number {
    return Math.ceil(this.komentari.length/this.brojPoStrani);
  }

  promeniStranicu(nova: number): void {
    if (nova >= 1 && nova <= this.ukupnoStranica()) {
      this.trenutnaStranica = nova;
    }
  }

  getZvezdice(): string[] {
    const ocena = this.vikendica.prosecna_ocena;
    const zvezdice: string[] = [];

    for (let i = 1; i <= 5; i++) {
      if (ocena >= i) {
        zvezdice.push('fas fa-star'); //puna zvezda
      } else if (ocena >= i - 0.5) {
        zvezdice.push('fas fa-star-half-alt'); //pola
      } else {
        zvezdice.push('far fa-star'); //prazna zvezda
      }
    }
    return zvezdice;
  }

  zakazi(){
    if(!this.vikendica.id) return;
    this.ruter.navigate(['turista/zakazivanje', this.vikendica.id]);
  }

}
