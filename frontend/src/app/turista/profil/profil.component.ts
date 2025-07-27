import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
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

  constructor(private turistaServis: TuristaService, private sanitizer: DomSanitizer) {}

  ngOnInit(): void {
    const korisnikLS = localStorage.getItem('korisnik');
    if (korisnikLS) {
      this.korisnik = JSON.parse(korisnikLS);
      this.slika = `/assets/images/${this.korisnik.profilna_slika_path}`;
    }
  }
  
  korisnik: KorisnikLoginResponse = new KorisnikLoginResponse();
  slika: string = '';
  novaSlika: File | null = null;

  izaberiSliku(event: any) {
    if (event.target.files && event.target.files.length > 0) {
      this.novaSlika = event.target.files[0];
    }
  }

  azuriraj() {
    this.turistaServis.azurirajKorisnika(this.korisnik, this.novaSlika).subscribe(res => {
      if (res) {
        alert('Uspešno ažurirano!');
        localStorage.setItem('korisnik', JSON.stringify(this.korisnik));
      } else {
        alert('Greška pri ažuriranju.');
      }
    });
  }
}
