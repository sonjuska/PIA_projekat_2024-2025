import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Vikendica } from '../models/vikendica';
import { StatistikaService } from './statistika.service';
import { VikendicaService } from '../vikendica.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-pocetna',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './pocetna.component.html',
  styleUrl: './pocetna.component.css'
})
export class PocetnaComponent {

  ngOnInit(): void {
    this.ucitajStatistiku();
    this.ucitajVikendice();
  }
  
  ukupnoVikendica = 0;
  ukupnoVlasnika = 0;
  ukupnoTurista = 0;
  rezervacije24h = 0;
  rezervacije7dana = 0;
  rezervacije30dana = 0;

  vikendice: Vikendica[] = [];
  pretragaNaziv = '';
  pretragaMesto = '';

  sortKolona: string = '';
  sortSmer: 'asc' | 'desc' = 'asc';

  private statistikaService = inject(StatistikaService);
  private vikendicaService = inject(VikendicaService);
  private ruter = inject(Router)


  ucitajStatistiku() {
    this.statistikaService.getUkupanBrojVikendica().subscribe(broj => this.ukupnoVikendica = broj);
    this.statistikaService.getUkupanBrojVlasnika().subscribe(broj => this.ukupnoVlasnika = broj);
    this.statistikaService.getUkupanBrojTurista().subscribe(broj => this.ukupnoTurista = broj);
    this.statistikaService.getBrojRezervacija24h().subscribe(broj => this.rezervacije24h = broj);
    this.statistikaService.getBrojRezervacija7dana().subscribe(broj => this.rezervacije7dana = broj);
    this.statistikaService.getBrojRezervacija30dana().subscribe(broj => this.rezervacije30dana = broj);
  }

  ucitajVikendice() {
    this.vikendicaService.getVikendice().subscribe(data => {
      this.vikendice = data;
      this.sortKolona = 'naziv';
      this.sortSmer = 'asc';
      this.sortiraj(this.sortKolona);
    });
  }

  pretrazi() {
    this.vikendicaService.pretraziVikendice(this.pretragaNaziv, this.pretragaMesto).subscribe(data => {
      this.vikendice = data;
    });
  }

  sortiraj(kolona: string) {
    if (this.sortKolona === kolona) {
      this.sortSmer = this.sortSmer === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortKolona = kolona;
      this.sortSmer = 'asc';
    }

    this.vikendice.sort((a, b) => {
      const vrednostA = (a as any)[kolona]?.toLowerCase() || '';
      const vrednostB = (b as any)[kolona]?.toLowerCase() || '';

      if (vrednostA < vrednostB) return this.sortSmer === 'asc' ? -1 : 1;
      if (vrednostA > vrednostB) return this.sortSmer === 'asc' ? 1 : -1;
      return 0;
    });
  }

  login(){
    this.ruter.navigate(['prijava'])
  }

  registracija(){
    this.ruter.navigate(['registracija'])
  }
}
