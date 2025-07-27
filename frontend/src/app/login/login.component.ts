import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { LoginService } from './login.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  korime: string = '';
  lozinka: string = '';
  porukaGreske: string = '';

  ruter = inject(Router)
  loginServis = inject(LoginService)

  login(): void {
    this.loginServis.loginKorisnik(this.korime, this.lozinka).subscribe((korisnik)=>{
      console.log(korisnik)
      if(korisnik){
        localStorage.setItem('korisnik', JSON.stringify(korisnik));
        if(korisnik.uloga=='turista')
          this.ruter.navigate(['/turista']);
        else
          this.ruter.navigate(['/vlasnik']);

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
