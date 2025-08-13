import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { vikendicaPoslednje3OceneManjeOd2 } from '../../models/vikendicaPoslednje3OceneManjeOd2';
import { AdminService } from '../admin.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-vikendice',
  standalone: true,
  imports: [FormsModule, CommonModule, RouterModule],
  templateUrl: './vikendice.component.html',
  styleUrl: './vikendice.component.css'
})
export class VikendiceAdminComponent implements OnInit{

  ngOnInit(): void {
    this.adminServis.dohvatiVikendice().subscribe(vik=>{
      this.vikendice = vik;
    })
  }

  vikendice: vikendicaPoslednje3OceneManjeOd2[] = [];
  adminServis = inject(AdminService);
  ruter = inject(Router)

  blokiraj(id: number) {
    this.adminServis.blokiraj(id).subscribe(res=>{
      if(res.uspeh){
        Swal.fire({
          title: 'Uspeh!',
          text: res.poruka,
          icon: 'success',
          confirmButtonText: 'Zatvori',
          confirmButtonColor: '#72522bff'
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
}
