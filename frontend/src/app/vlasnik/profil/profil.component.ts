import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import Swal from 'sweetalert2';
import { KorisnikLoginResponse } from '../../responses/KorisnikLoginResponse';
import { VlasnikService } from '../vlasnik.service';

@Component({
  selector: 'app-profil',
  standalone: true,
  imports: [RouterModule, CommonModule, FormsModule],
  templateUrl: './profil.component.html',
  styleUrl: './profil.component.css'
})
export class ProfilComponent {
  constructor() {}

  ngOnInit(): void {
    const korisnikLS = localStorage.getItem('korisnik');
    if (korisnikLS) {
      let k = JSON.parse(korisnikLS);
      this.vlasnikServis.dohvatiKorisnika(k.korisnicko_ime).subscribe(korisnik=>{
        if(korisnik){
          this.korisnik = korisnik;

          if (this.korisnik.profilna_slika_path) {
            this.slikaPreview = 'http://localhost:8080/' + this.korisnik.profilna_slika_path;
          }
        }
      })
    }
  }

  korisnik: KorisnikLoginResponse = new KorisnikLoginResponse();
  novaSlika: File | null = null;

  vlasnikServis = inject(VlasnikService)
  ruter = inject(Router)

  slikaPreview: string | null = null;
  slikaUklonjena = false;

  izaberiSliku(event: Event) {
    const input = event.target as HTMLInputElement;
    if (!input.files || input.files.length === 0) return;

    const file = input.files[0];
    this.novaSlika = file;

    const reader = new FileReader();
    reader.onload = (e: any) => {
      this.slikaPreview = e.target.result;
    };
    reader.readAsDataURL(file);
  }

  ukloniSliku() {
    this.novaSlika = null;
    this.slikaPreview = null;
    this.korisnik.profilna_slika_path = ''; 
    this.slikaUklonjena = true;
  }
  azuriraj() {
    this.vlasnikServis.azurirajKorisnika(this.korisnik, this.novaSlika, this.slikaUklonjena).subscribe(res => {
      if (res) {
        Swal.fire({
          title: 'Uspeh!',
          text: 'Uspešno ažurirano!',
          icon: 'success',
          confirmButtonText: 'U redu',
          confirmButtonColor: '#72522bff'
        }).then(() => {
          this.vlasnikServis.dohvatiKorisnika(this.korisnik.korisnicko_ime).subscribe(korisnik => {
            if (korisnik) {
              this.korisnik = korisnik;
              localStorage.setItem('korisnik', JSON.stringify(korisnik));
              window.location.reload();
            }
          });
        });
      }else{
        Swal.fire({
          title: 'Greška!',
          text: 'Greška pri ažuriranju.',
          icon: 'error',
          confirmButtonText: 'Zatvori',
          confirmButtonColor: '#72522bff'
        });
      }
    });
  }
}
