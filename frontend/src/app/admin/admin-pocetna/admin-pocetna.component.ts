import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { Router, RouterModule, RouterOutlet } from '@angular/router';
import { KorisnikLoginResponse } from '../../responses/KorisnikLoginResponse';

@Component({
  selector: 'app-admin-pocetna',
  standalone: true,
  imports: [RouterModule, CommonModule],
  templateUrl: './admin-pocetna.component.html',
  styleUrl: './admin-pocetna.component.css'
})
export class AdminPocetnaComponent implements OnInit{
  ngOnInit(): void {

  let a = localStorage.getItem('admin')
    if(a){
      this.admin = JSON.parse(a);
      console.log(this.admin)
    }
  }

  admin: KorisnikLoginResponse = new KorisnikLoginResponse()
  ruter = inject(Router)

  odjava(){
    localStorage.removeItem('admin');
    this.ruter.navigate(['']);
  }
}
