import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { PromenaLozinkeService } from './promena-lozinke.service';

@Component({
  selector: 'app-promena-lozinke',
  standalone: true,
  imports: [FormsModule, RouterModule, CommonModule],
  templateUrl: './promena-lozinke.component.html',
  styleUrl: './promena-lozinke.component.css'
})
export class PromenaLozinkeComponent {
  korisnicko_ime: string = '';
  staraLozinka: string = '';
  novaLozinka: string = '';
  ponovljenaNovaLozinka: string = '';
  porukaGreske: string = '';

  ruter = inject(Router)
  promenaLozinkeServis = inject(PromenaLozinkeService)

  promeniLozinku() {
    if(this.staraLozinka == this.novaLozinka){
      this.porukaGreske = "Stara i nova lozinka ne smeju biti iste.";
      return;
    }
    if(this.novaLozinka != this.ponovljenaNovaLozinka){
      this.porukaGreske = "Nova i ponovljena nova lozinka nisu iste.";
      return;
    }

    this.promenaLozinkeServis.promeniLozinku(this.korisnicko_ime, this.staraLozinka, this.novaLozinka).subscribe(res=>{
      if(res.promenjena){
        alert(res.poruka);
        if(res.daLiJeAdmin){
          this.ruter.navigate(['/admin/prijava'])
        }else{
          this.ruter.navigate(['/prijava'])
        }

      }else{
        this.porukaGreske = res.poruka;
      }
    })
  }

  nazad(){
    this.ruter.navigate([''])
  }
}
