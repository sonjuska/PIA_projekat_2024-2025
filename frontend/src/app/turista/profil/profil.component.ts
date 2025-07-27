import { LoginService } from './../../login/login.service';
import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { KorisnikLoginResponse } from '../../responses/KorisnikLoginResponse';
import { DomSanitizer } from '@angular/platform-browser';
import { TuristaService } from '../turista.service';

@Component({
  selector: 'app-profil',
  standalone: true,
  imports: [RouterModule, FormsModule, CommonModule],
  templateUrl: './profil.component.html',
  styleUrl: './profil.component.css'
})
export class ProfilComponent {

  constructor(private sanitizer: DomSanitizer) {}

  ngOnInit(): void {
    const korisnikLS = localStorage.getItem('korisnik');
    if (korisnikLS) {
      let k = JSON.parse(korisnikLS);
      this.turistaServis.dohvatiKorisnika(k.korisnicko_ime).subscribe(korisnik=>{
        if(korisnik){
          this.korisnik = korisnik;
        }
      })
    }
  }

  korisnik: KorisnikLoginResponse = new KorisnikLoginResponse();
  novaSlika: File | null = null;

  turistaServis = inject(TuristaService)
  ruter = inject(Router)

  izaberiSliku(event: any) {
    if (event.target.files && event.target.files.length > 0) {
      this.novaSlika = event.target.files[0];
    }
  }

  azuriraj() {
    this.turistaServis.azurirajKorisnika(this.korisnik, this.novaSlika).subscribe(res => {
      if (res) {
        alert('Uspešno ažurirano!');
        this.turistaServis.dohvatiKorisnika(this.korisnik.korisnicko_ime).subscribe(korisnik=>{
          if(korisnik){
            this.korisnik = korisnik
            localStorage.setItem('korisnik', JSON.stringify(korisnik))
            this.ruter.navigate(['/turista/profil']);

          }
        })

      } else {
        alert('Greška pri ažuriranju.');
      }
    });
  }
}
