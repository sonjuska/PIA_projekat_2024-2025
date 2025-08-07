import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { ZahtevZaRegistraciju } from '../../models/zahtevZaRegistraciju';
import { Korisnik } from '../../models/korisnik';
import { AdminService } from '../admin.service';
import { FormsModule } from '@angular/forms';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-zahtevi-za-registraciju',
  standalone: true,
  imports: [RouterModule, CommonModule, FormsModule],
  templateUrl: './zahtevi-za-registraciju.component.html',
  styleUrl: './zahtevi-za-registraciju.component.css'
})
export class ZahteviZaRegistracijuComponent implements OnInit{
  ngOnInit(): void {
    let a = localStorage.getItem('admin')
    if(a){
      this.admin = JSON.parse(a);

      this.adminServis.sviZahteviZaRegistraciju().subscribe(zahtevi=>{
        if(zahtevi.length>0){
          this.zahtevi = zahtevi
        }
      })
    }
  }

  admin: Korisnik = new Korisnik()
  zahtevi: ZahtevZaRegistraciju[] = []
  odbijen: boolean = false
  spremanZaOdbijanje: boolean = false;
  komentar: string = ''
  odbijeniId: number | null = null;
  komentari: { [id: number]: string } = {};


  ruter = inject(Router)
  adminServis = inject(AdminService)

  odobri(id: number){
      Swal.fire({
        title: 'Da li ste sigurni?',
        text: 'Potrvdite da biste odobrili zahtev za registraciju korisnika.',
        icon: 'info',
        confirmButtonText: 'Potvrdi',
        confirmButtonColor: '#72522bff'
      }).then(rez=>{
        if(rez.isConfirmed){
          this.adminServis.odobri(id).subscribe(res=>{
            if(res){
              this.adminServis.sviZahteviZaRegistraciju().subscribe(zahtevi=>{
                if(zahtevi.length>0){
                  this.zahtevi = zahtevi
                  Swal.fire({
                    title: 'Uspeh!',
                    text: 'Zahtev za registraciju je odobren.',
                    icon: 'success',
                    confirmButtonText: 'Zatvori',
                    confirmButtonColor: '#72522bff'
                  })
                }
              })
            }
          })

        }
      });

  }
  prikaziKomentar(id: number) {
    this.odbijeniId = id;
  }

  spreman(id: number) {
    const komentar = this.komentari[id] || '';
    this.adminServis.odbij(id, komentar).subscribe(res => {
      if (res) {
        this.adminServis.sviZahteviZaRegistraciju().subscribe(zahtevi => {
          this.zahtevi = zahtevi;
          this.odbijeniId = null;
        });
      }
    });
  }

  odbij(id: number) {
    this.odbijeniId = id; 
  }

}
