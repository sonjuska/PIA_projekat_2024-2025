import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { VlasnikService } from '../vlasnik.service';
import { DohvatiRezervacijuResponse } from '../../responses/DohvatiRezervacijuResponse';
import { KorisnikLoginResponse } from '../../responses/KorisnikLoginResponse';
import { RezervacijeService } from './rezervacije.service';
import Swal from 'sweetalert2';


@Component({
  selector: 'app-rezervacije',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './rezervacije.component.html',
  styleUrl: './rezervacije.component.css'
})
export class RezervacijeComponent implements OnInit{
  ngOnInit(): void {
    let k = localStorage.getItem('korisnik');
    if(k){
      let korisnik = JSON.parse(k);
      this.vlasnikServis.dohvatiKorisnika(korisnik.korisnicko_ime).subscribe(kor=>{
        if(kor){
          this.korisnik = kor;
          this.rezervacijaServis.dohvatiMojeRezervacije(this.korisnik.korisnicko_ime).subscribe(rez=>{
            this.rezervacije = rez;
            for(let r of this.rezervacije){
              if(r.status=='na_cekanju') this.rezervacijeNaCekanju.push(r);
            }
          })
        }
      })
    }
  }

  vlasnikServis = inject(VlasnikService)
  rezervacijaServis = inject(RezervacijeService)
  ruta = inject(ActivatedRoute)

  korisnik: KorisnikLoginResponse = new KorisnikLoginResponse();
  rezervacije: DohvatiRezervacijuResponse[] = [];
  rezervacijeNaCekanju: DohvatiRezervacijuResponse[] = [];
  trenutniIdKomentara: number = -1;
  porukaGreske: string = '';

  odobri(id: number){
    this.rezervacijaServis.potvrdiRezervaciju(id).subscribe(res=>{
      if(res.uspesna){
        Swal.fire({
          title: 'Uspeh!',
          text: res.poruka,
          icon: 'success',
          confirmButtonText: 'U redu',
          confirmButtonColor: '#72522bff'
        });
        this.rezervacijaServis.dohvatiMojeRezervacije(this.korisnik.korisnicko_ime).subscribe(rez => {
          this.rezervacije = rez;
          this.rezervacijeNaCekanju = [];
          for (let r of this.rezervacije) {
            if (r.status == 'na_cekanju') this.rezervacijeNaCekanju.push(r);
          }
        });

      }else{
        Swal.fire({
          title: 'Greška!',
          text: res.poruka,
          icon: 'error',
          confirmButtonText: 'Zatvori',
          confirmButtonColor: '#72522bff'
        });
      }
    })
  }
  odbijUnesiKomentar(id: number){
    if(this.trenutniIdKomentara != id) this.trenutniIdKomentara = id;
    else this.trenutniIdKomentara = -1;
  }
  odbij(id: number, komentar: string){
    if(!komentar || komentar.trim() === ''){
      this.porukaGreske = "Morate uneti komentar odbijanja.";
      return;
    }
    this.trenutniIdKomentara = -1;
    this.porukaGreske = '';
    this.rezervacijaServis.odbijRezervaciju(id, komentar).subscribe(res=>{
      if(res.uspesna){
        Swal.fire({
          title: 'Uspeh!',
          text: res.poruka,
          icon: 'success',
          confirmButtonText: 'U redu',
          confirmButtonColor: '#72522bff'
        });
        this.rezervacijaServis.dohvatiMojeRezervacije(this.korisnik.korisnicko_ime).subscribe(rez => {
          this.rezervacije = rez;
          this.rezervacijeNaCekanju = []; // <--- resetovanje pre punjenja
          for (let r of this.rezervacije) {
            if (r.status == 'na_cekanju') this.rezervacijeNaCekanju.push(r);
          }
        });
      }else{
        Swal.fire({
          title: 'Greška!',
          text: res.poruka,
          icon: 'error',
          confirmButtonText: 'Zatvori',
          confirmButtonColor: '#72522bff'
        });
      }
    })
  }
}
