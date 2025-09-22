import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RegistracijaService } from './registracija.service';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-registracija',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './registracija.component.html',
  styleUrl: './registracija.component.css'
})
export class RegistracijaComponent {
  korime : string = '';
  lozinka : string = '';
  ime : string = '';
  prezime : string = '';
  pol : string = '';
  adresa : string = '';
  telefon : string = '';
  email : string = '';
  brojKartice : string = '';
  uloga : string = '';
  slika : File | null = null;

  validnaLozinka = false;
  validnaKartica = false;
  karticaTip: string | null = null;
  karticaIkonica: string | null = null;

  porukaGreske: string = '';
  slikaPoruka: string = '';
  karticaPoruka: string = ''

  registracijaServis = inject(RegistracijaService)
  ruter = inject(Router)

  proveriLozinku() {
    const regex = /^(?=[A-Za-z])(?=.*[A-Z])(?=(?:.*[a-z]){3,})(?=.*\d)(?=.*[!@#\$%\^&\*\-_\+=]).{6,10}$/;
    this.validnaLozinka = regex.test(this.lozinka);
  }

  proveriKarticu() {
    const broj = this.brojKartice;
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
    if(!this.validnaKartica && this.brojKartice.length>16) this.karticaPoruka = 'Kartica nije validna.'
    if(this.brojKartice.length<15 || this.validnaKartica) this.karticaPoruka = ''
  }

  izabranaSlika(event: any) {
    this.slikaPoruka = '';
    if (event.target.files.length > 0) {
      const file = event.target.files[0];
      const validTypes = ['image/jpeg', 'image/png'];

      if (!validTypes.includes(file.type)) {
        this.slikaPoruka = 'Dozvoljeni su samo JPG i PNG formati.';
        this.slika = null;
        return;
      }

      const reader = new FileReader();
      reader.onload = (e: any) => {
        const img = new Image();
        img.onload = () => {
          if (
            img.width < 100 || img.height < 100 ||
            img.width > 300 || img.height > 300
          ) {
            this.slikaPoruka = 'Slika mora biti dimenzija između 100x100 i 300x300 piksela.';
            this.slika = null;
          } else {
            this.slika = file;
          }
        };
        img.src = e.target.result;
      };

      reader.readAsDataURL(file);
    }
  }


  registruj() {
    if (!this.validnaLozinka || !this.validnaKartica) {
      this.porukaGreske = 'Proverite lozinku i broj kartice.';
      return;
    }

    this.registracijaServis.registruj(this.korime, this.lozinka, this.ime, this.prezime, this.pol, this.adresa, this.telefon,
      this.email, this.brojKartice, this.uloga, this.slika
    ).subscribe(res=>{
      if(!res.registrovan){
        this.porukaGreske = res.poruka
      }else{
        Swal.fire({
          title: 'Uspeh!',
          text: res.poruka,
          icon: 'success',
          confirmButtonText: 'U redu',
          confirmButtonColor: '#72522bff'
        });
        this.ruter.navigate(['']);
      }
    })
  }

  dozvoliSamoBrojeve(event: KeyboardEvent): void {
    const charCode = event.key.charCodeAt(0);
    if (charCode < 48 || charCode > 57) {
      event.preventDefault();
    }
  }

  nazad(){
    this.ruter.navigate(['/'], {
      queryParams: {},
      replaceUrl: true
    })

  }

  dozvoliBrojeveIPlus(event: KeyboardEvent) {
    const char = event.key;
    if (char.length > 1) return;
    if (/^\d$/.test(char)) return;
    if (char === '+' && (!this.telefon || this.telefon.length === 0)) return;
    event.preventDefault();
  }

  ocistiTelefon() {
    if (!this.telefon) return;
    this.telefon = this.telefon.replace(/[^\d+]/g, '');
    if (this.telefon.indexOf('+') > 0) {
      this.telefon = this.telefon.replace(/\+/g, '');
    }
  }

}
