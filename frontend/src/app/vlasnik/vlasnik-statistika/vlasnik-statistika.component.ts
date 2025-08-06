import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { Color, NgxChartsModule, ScaleType } from '@swimlane/ngx-charts'
import { KorisnikLoginResponse } from '../../responses/KorisnikLoginResponse';
import { VlasnikStatistikaService } from './vlasnik-statistika.service';
import { ChartData } from '../../responses/KolonaDijagramPodaci';
import { PieData } from '../../responses/PitaDijagramPodaci';
import { LegendPosition } from '@swimlane/ngx-charts';

@Component({
  selector: 'app-vlasnik-statistika',
  standalone: true,
  imports: [NgxChartsModule, CommonModule, FormsModule, RouterModule],
  templateUrl: './vlasnik-statistika.component.html',
  styleUrl: './vlasnik-statistika.component.css'
})
export class VlasnikStatistikaComponent implements OnInit{
  ngOnInit(): void {
    let t = localStorage.getItem('korisnik')
    if(t){
      this.vlasnik = JSON.parse(t);

      this.statistikaServis.dohvatiPodatkeZaDijagramKolona(this.vlasnik.korisnicko_ime).subscribe(podaci=>{
        this.podaciKolonaDijagram = podaci;
        //console.log(podaci)
      })

      this.statistikaServis.dohvatiPodatkeDijagramPita(this.vlasnik.korisnicko_ime).subscribe(podaci => {
        this.podaciPitaDijagram = podaci;
        //console.log(this.podaciPitaDijagram)
        this.pitaDijagrami = podaci.map(pd => ({
          title: pd.name,
          results: [
            {
              name: `Vikend`,
              value: pd.brojRezervacijaVikend
            },
            {
              name: `Radni dan`,
              value: pd.brojRezervacijaRadniDani
            }
          ]
        }));

  });

    }
  }

  vlasnik: KorisnikLoginResponse = new KorisnikLoginResponse();
  ruter = inject(Router)
  statistikaServis = inject(VlasnikStatistikaService)
  podaciKolonaDijagram: ChartData[] = [];
  podaciPitaDijagram: PieData[] = [];

  //za pita dijagram
  pitaDijagrami: { title: string; results: { name: string; value: number }[] }[] = [];
  legendPosition: LegendPosition = LegendPosition.Below;

  lambdaZaLabelu(name: string){
    return name;
  }


  //za dijagram kolona
  view: [number, number] = [700, 500];
  showXAxis = true;
  showYAxis = true;
  gradient = false;
  showLegend = true;
  showXAxisLabel = true;
  xAxisLabel = 'Mesec';
  showYAxisLabel = true;
  yAxisLabel = 'Broj rezervacija';

  colorScheme: Color = {
    name: 'braon-kontrast',
    selectable: true,
    group: ScaleType.Ordinal,
    domain: [
      '#A0522D', 
      '#c8cd3fff', 
      '#8b3513ff', 
      '#e9a371ff', 
      '#59633fff', 
      '#3a0303ff', 
      '#e9e1afff', 
      '#f80808ff'
    ]
  };


}
