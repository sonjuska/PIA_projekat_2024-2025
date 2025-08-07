import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { KorisnikLoginResponse } from '../../responses/KorisnikLoginResponse';
import { AdminService } from '../admin.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-korisnici',
  standalone: true,
  imports: [RouterModule, CommonModule],
  templateUrl: './korisnici.component.html',
  styleUrl: './korisnici.component.css'
})
export class KorisniciComponent implements OnInit{
  ngOnInit(): void {
    let a = localStorage.getItem('admin')
      if(a){
        this.admin = JSON.parse(a);
        this.adminServis.dohvatiKorisnike().subscribe(kor=>{
          this.korisnici = kor;
        })
      }
  }

  adminServis = inject(AdminService)
  ruter = inject(Router)

  korisnici: KorisnikLoginResponse[] = []
  admin: KorisnikLoginResponse = new KorisnikLoginResponse();

  azuriraj(k: KorisnikLoginResponse){
    this.ruter.navigate(['admin/korisnici/uredi-korisnika/', k.korisnicko_ime])
  }
  deaktiviraj(k: KorisnikLoginResponse){
    Swal.fire({
      title: 'Da li ste sigurni?',
      text: 'Potrvdite da biste deaktivirali korisnika.',
      icon: 'info',
      confirmButtonText: 'Potvrdi',
      confirmButtonColor: '#72522bff'
    }).then(rez=>{
      if(rez.isConfirmed){
        this.adminServis.deaktivirajKorisnika(k.korisnicko_ime).subscribe(res=>{
          if(res.uspeh){
            Swal.fire({
              title: 'Uspeh!',
              text: res.poruka,
              icon: 'success',
              confirmButtonText: 'Zatvori',
              confirmButtonColor: '#72522bff'
            })
            this.adminServis.dohvatiKorisnike().subscribe(kor=>{
              this.korisnici = kor;
            })
          }else{
            Swal.fire({
              title: 'Greška!',
              text: res.poruka,
              icon: 'error',
              confirmButtonText: 'Zatvori',
              confirmButtonColor: '#72522bff'
            })
          }
        })

      }
    });
  }
}
