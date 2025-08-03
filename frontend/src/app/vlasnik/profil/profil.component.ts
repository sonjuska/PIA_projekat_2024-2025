import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DomSanitizer } from '@angular/platform-browser';
import { Router, RouterModule } from '@angular/router';
import Swal from 'sweetalert2';
import { KorisnikLoginResponse } from '../../responses/KorisnikLoginResponse';
import { TuristaService } from '../../turista/turista.service';
import { VlasnikService } from '../vlasnik.service';

@Component({
  selector: 'app-profil',
  standalone: true,
  imports: [RouterModule, CommonModule, FormsModule],
  templateUrl: './profil.component.html',
  styleUrl: './profil.component.css'
})
export class ProfilComponent {
  constructor(private sanitizer: DomSanitizer) {}

  ngOnInit(): void {
    const korisnikLS = localStorage.getItem('korisnik');
    if (korisnikLS) {
      let k = JSON.parse(korisnikLS);
      this.vlasnikServis.dohvatiKorisnika(k.korisnicko_ime).subscribe(korisnik=>{
        if(korisnik){
          this.korisnik = korisnik;
        }
      })
    }
  }

  korisnik: KorisnikLoginResponse = new KorisnikLoginResponse();
  novaSlika: File | null = null;

  vlasnikServis = inject(VlasnikService)
  ruter = inject(Router)

  izaberiSliku(event: any) {
    if (event.target.files && event.target.files.length > 0) {
      this.novaSlika = event.target.files[0];
    }
  }

  azuriraj() {
    this.vlasnikServis.azurirajKorisnika(this.korisnik, this.novaSlika).subscribe(res => {
      if (res) {
        Swal.fire({
          title: 'Uspeh!',
          text: 'Uspešno ažurirano!',
          icon: 'success',
          confirmButtonText: 'U redu',
          confirmButtonColor: '#72522bff'
        });
        this.vlasnikServis.dohvatiKorisnika(this.korisnik.korisnicko_ime).subscribe(korisnik=>{
          if(korisnik){
            this.korisnik = korisnik
            localStorage.setItem('korisnik', JSON.stringify(korisnik))
            this.ruter.navigate(['/vlasnik/profil']);
          }
        })

      } else {
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
