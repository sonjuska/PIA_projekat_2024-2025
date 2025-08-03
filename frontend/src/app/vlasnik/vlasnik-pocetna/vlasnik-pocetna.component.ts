import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { KorisnikLoginResponse } from '../../responses/KorisnikLoginResponse';

@Component({
  selector: 'app-vlasnik-pocetna',
  standalone: true,
  imports: [RouterModule, CommonModule, FormsModule],
  templateUrl: './vlasnik-pocetna.component.html',
  styleUrl: './vlasnik-pocetna.component.css'
})
export class VlasnikPocetnaComponent {
  timestamp: number = Date.now();

  ngOnInit(): void {
    let t = localStorage.getItem('korisnik')
      if(t){
        this.vlasnik = JSON.parse(t);
    }
  }

  vlasnik: KorisnikLoginResponse = new KorisnikLoginResponse()
  ruter = inject(Router)

  osveziProfilnuSliku() {
    const t = localStorage.getItem('korisnik');
    if (t) {
      this.vlasnik = JSON.parse(t);
      this.timestamp = Date.now(); //primorava sliku da se ucita ponovo
    }
  }

  odjava(){
    localStorage.removeItem('korisnik');
    this.ruter.navigate(['']);
  }
}
