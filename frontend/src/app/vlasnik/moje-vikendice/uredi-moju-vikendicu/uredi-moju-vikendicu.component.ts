import { Component, inject, OnInit } from '@angular/core';
import { MojeVikendiceService } from '../moje-vikendice.service';
import { Vikendica } from '../../../models/vikendica';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-uredi-moju-vikendicu',
  standalone: true,
  imports: [FormsModule, CommonModule, RouterModule],
  templateUrl: './uredi-moju-vikendicu.component.html',
  styleUrl: './uredi-moju-vikendicu.component.css'
})
export class UrediMojuVikendicuComponent implements OnInit {
  ngOnInit(): void {
    const id = this.ruta.snapshot.paramMap.get('id');
    if (id) {
      this.vikendicaServis.getVikendicaPoId(+id).subscribe(vik => {
        if (vik) {
          this.vikendica = vik;
          this.usluge = this.vikendica.usluge ? this.vikendica.usluge.split(',').map(u => u.trim()) : [];

          this.vikendicaServis.getSlikeVikendice(vik.id).subscribe(slikePutanje => {
          this.stareSlike = slikePutanje; 
          this.slikePreview = slikePutanje.map(p => `http://localhost:8080/${p}`);
        });
        }
      });
    }
  }

  ruta = inject(ActivatedRoute)
  vikendicaServis = inject(MojeVikendiceService)
  vikendica: Vikendica = new Vikendica();
  novaUsluga: string = '';
  usluge: string[] = [];

  azuriraj(){
  this.vikendica.usluge = this.usluge.join(',');

  this.vikendicaServis.azurirajVikendicu(this.vikendica, this.slike, this.obrisaneSlike).subscribe(res=>{
    if(res.uspeh){
      Swal.fire({
        title: 'Uspeh!',
        text: res.poruka,
        icon: 'success',
        confirmButtonText: 'U redu',
        confirmButtonColor: '#72522bff'
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
  dozvoliSamoBrojeve(event: KeyboardEvent) {
    const char = event.key;

    //dozvoli samo cifre i + kao prvi karakter
    const isFirstCharPlus = char === '+' && (event.target as HTMLInputElement).selectionStart === 0;
    const isDigit = /^[0-9]$/.test(char);

    if (!isDigit && !isFirstCharPlus) {
      event.preventDefault();
    }
  }
  dodajUslugu() {
    const usluga = this.novaUsluga.trim();
    if (usluga && !this.usluge.includes(usluga)) {
      this.usluge.push(usluga);
    }
    this.novaUsluga = '';
  }

  ukloniUslugu(index: number) {
    this.usluge.splice(index, 1);
  }
  slike: string[] = []; 
  slikePreview: string[] = []; 
  stareSlike: string[] = [];      
  obrisaneSlike: string[] = [];

  onSlikeSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (!input.files) return;

    Array.from(input.files).forEach(file => {
      const reader = new FileReader();
      reader.onload = (e: any) => {
        const slikaURL = e.target.result as string;
        this.slike.push(slikaURL); 
        this.slikePreview.push(slikaURL);
      };
      reader.readAsDataURL(file);
    });
  }
  ukloniSliku(index: number) {
  let slikaZaBrisanje = this.slikePreview[index];

  let relPath = this.stareSlike.find(p => `http://localhost:8080/${p}` === slikaZaBrisanje);
  if (relPath) {
    this.obrisaneSlike.push(relPath);
    this.stareSlike = this.stareSlike.filter(p => p !== relPath);
  } else {
    let idxNove = this.slike.indexOf(slikaZaBrisanje);
    if (idxNove >= 0) {
      this.slike.splice(idxNove, 1);
    }
  }

  this.slikePreview.splice(index, 1);
}


}
