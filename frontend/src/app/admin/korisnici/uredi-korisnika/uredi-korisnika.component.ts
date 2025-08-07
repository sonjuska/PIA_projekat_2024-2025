import { Component, inject, OnInit } from '@angular/core';
import { KorisnikLoginResponse } from '../../../responses/KorisnikLoginResponse';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { AdminService } from '../../admin.service';
import { TuristaService } from '../../../turista/turista.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-uredi-korisnika',
  standalone: true,
  imports: [FormsModule, CommonModule, RouterModule],
  templateUrl: './uredi-korisnika.component.html',
  styleUrl: './uredi-korisnika.component.css'
})
export class UrediKorisnikaComponent implements OnInit{

  ngOnInit(): void {
    const id = this.ruta.snapshot.paramMap.get('id');
    if (id) {
      this.adminServis.dohvatiKorisnikaPoKorisnickomImenu(id).subscribe(korisnik=>{
        if(korisnik){
          this.korisnik = korisnik;

          if (this.korisnik.profilna_slika_path) {
            this.slikaPreview = 'http://localhost:8080/' + this.korisnik.profilna_slika_path;
          }
          let admin = localStorage.getItem('admin');
          if(admin){
            let a = JSON.parse(admin);
            this.adminServis.dohvatiKorisnikaPoKorisnickomImenu(a.korisnicko_ime).subscribe(ad=>{
              this.admin = ad;
            })
          }

        }
      })
    }
  }

  ruta = inject(ActivatedRoute)
  adminServis = inject(AdminService)

  korisnik: KorisnikLoginResponse = new KorisnikLoginResponse();
  admin: KorisnikLoginResponse = new KorisnikLoginResponse();
  novaSlika: File | null = null;
  slikaPreview: string | null = null;
  slikaUklonjena = false;
  
  izaberiSliku(event: Event) {
    let input = event.target as HTMLInputElement;
    if (!input.files || input.files.length === 0) return;

    let file = input.files[0];
    this.novaSlika = file;

    let reader = new FileReader();
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

  sacuvajIzmene() {
    this.adminServis.azurirajKorisnika(this.korisnik, this.novaSlika, this.slikaUklonjena).subscribe(res=>{
      if(res.uspeh){
        Swal.fire({
          title: 'Uspeh!',
          text: 'Uspešno ažurirano!',
          icon: 'success',
          confirmButtonText: 'U redu',
          confirmButtonColor: '#72522bff'
        }).then(() => {
          this.adminServis.dohvatiKorisnikaPoKorisnickomImenu(this.korisnik.korisnicko_ime).subscribe(korisnik => {
            if (korisnik) {
              this.korisnik = korisnik;
              if(this.korisnik.korisnicko_ime == this.ruta.snapshot.paramMap.get('id')){
                localStorage.setItem('admin', JSON.stringify(this.korisnik))
              }

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
    })
  }
}
