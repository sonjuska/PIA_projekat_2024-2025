import { Component, inject, OnInit } from '@angular/core';
import { Router, RouterModule } from "@angular/router";
import { KorisnikLoginResponse } from '../../responses/KorisnikLoginResponse';

@Component({
  selector: 'app-turista-pocetna',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './turista-pocetna.component.html',
  styleUrl: './turista-pocetna.component.css'
})
export class TuristaPocetnaComponent implements OnInit{
  timestamp: number = Date.now();

  ngOnInit(): void {
    let t = localStorage.getItem('korisnik')
      if(t){
        this.turista = JSON.parse(t);
    }
  }

  turista: KorisnikLoginResponse = new KorisnikLoginResponse()
  ruter = inject(Router)

  osveziProfilnuSliku() {
    const t = localStorage.getItem('korisnik');
    if (t) {
      this.turista = JSON.parse(t);
      this.timestamp = Date.now(); //primorava sliku da se ucita ponovo
    }
  }

  odjava(){
    localStorage.removeItem('korisnik');
    this.ruter.navigate(['']);
  }
}
