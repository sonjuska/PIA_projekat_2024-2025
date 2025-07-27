import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AdminLoginService } from './admin-login.service';

@Component({
  selector: 'app-admin-login',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './admin-login.component.html',
  styleUrl: './admin-login.component.css'
})
export class AdminLoginComponent {
  korime: string = '';
  lozinka: string = '';
  porukaGreske: string = '';

  ruter = inject(Router)
  adminLoginServis = inject(AdminLoginService)

  login(): void {

    this.adminLoginServis.loginAdmin(this.korime, this.lozinka).subscribe((admin)=>{
      console.log(admin)
      if(admin){
        localStorage.setItem('admin', JSON.stringify(admin));
        this.ruter.navigate(['admin']);

        this.porukaGreske = '';
      }
      else{
        this.porukaGreske = 'Pogrešno korisničko ime ili lozinka.';
      }
    });
  }
  nazad(){
    this.ruter.navigate([''])
  }
}
