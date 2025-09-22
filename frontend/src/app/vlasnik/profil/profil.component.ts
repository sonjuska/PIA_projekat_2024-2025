import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import Swal from 'sweetalert2';
import { KorisnikLoginResponse } from '../../responses/KorisnikLoginResponse';
import { VlasnikService } from '../vlasnik.service';

@Component({
  selector: 'app-vlasnik-profil',
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

  validnaKartica = false;
  karticaTip: string | null = null;
  karticaIkonica: string | null = null;
  karticaPoruka: string | null = null;

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

  proveriKarticu() {
    const broj = this.korisnik.broj_kartice;
    this.karticaTip = null;
    this.karticaIkonica = null;
    this.validnaKartica = false;

    if (/^(30[0-3]\d{12}|36\d{13}|38\d{13})$/.test(broj)) {
      this.karticaTip = 'Diners';
      this.karticaIkonica = '/diners.png';
      this.validnaKartica = true;
    } else if (/^5[1-5]\d{14}$/.test(broj)) {
      this.karticaTip = 'MasterCard';
      this.karticaIkonica = '/mastercard.png';
      this.validnaKartica = true;
    } else if (/^(4539|4556|4916|4532|4929|4485|4716)\d{12}$/.test(broj)) {
      this.karticaTip = 'Visa';
      this.karticaIkonica = '/visa.png';
      this.validnaKartica = true;
    }
    if(!this.validnaKartica && this.korisnik.broj_kartice.length>16) this.karticaPoruka = 'Kartica nije validna.'
    if(this.korisnik.broj_kartice.length<15 || this.validnaKartica) this.karticaPoruka = ''
  }

  dozvoliSamoBrojeve(event: KeyboardEvent): void {
    const charCode = event.key.charCodeAt(0);
    if (charCode < 48 || charCode > 57) {
      event.preventDefault();
    }
  }

  dozvoliBrojeveIPlus(event: KeyboardEvent) {
    const char = event.key;
    if (char.length > 1) return;
    if (/^\d$/.test(char)) return;
    if (char === '+' && (!this.korisnik.telefon || this.korisnik.telefon.length === 0)) return;
    event.preventDefault();
  }

  ocistiTelefon() {
    if (!this.korisnik.telefon) return;
    this.korisnik.telefon = this.korisnik.telefon.replace(/[^\d+]/g, '');
    if (this.korisnik.telefon.indexOf('+') > 0) {
      this.korisnik.telefon = this.korisnik.telefon.replace(/\+/g, '');
    }
  }
}
