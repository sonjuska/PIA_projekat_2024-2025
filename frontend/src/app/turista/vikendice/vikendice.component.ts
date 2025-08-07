import { Component, inject, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { VikendicaService } from './vikendica.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Vikendica } from '../../models/vikendica';
import { MatTooltipModule } from '@angular/material/tooltip';

@Component({
  selector: 'app-vikendice',
  standalone: true,
  imports: [FormsModule, CommonModule, RouterModule, MatTooltipModule],
  templateUrl: './vikendice.component.html',
  styleUrl: './vikendice.component.css'
})
export class VikendiceComponent implements OnInit{
  ngOnInit(): void {
    this.ucitajVikendice();
  }
  
  vikendice: Vikendica[] = [];
  pretragaNaziv = '';
  pretragaMesto = '';
  ocena: String = 'nije ocenjena';

  sortKolona: string = '';
  sortSmer: 'asc' | 'desc' = 'asc';

  private vikendicaService = inject(VikendicaService);

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
  
  getZvezdice(ocena: number): string[] {
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

  daLiJeBlokirana(v: Vikendica): string {
    if (v.blokirana_do && new Date(v.blokirana_do) > new Date()) {
      return 'background-color: #f5a698;';
    }
    return '';
  }
  formatirajDatum(dateStr: string): string {
    let datum = new Date(dateStr);
    return datum.toLocaleDateString('sr-RS'); // format: 10.8.2025.
  }
  getTooltipZaVikendicu(v: Vikendica): string{
    if (v.blokirana_do && new Date(v.blokirana_do) > new Date()) {
      return 'Blokirana do: ' + this.formatirajDatum(v.blokirana_do);
    }
    return '';
  }




}
