import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { Vikendica } from '../../models/vikendica';
import { MojeVikendiceService } from './moje-vikendice.service';
import { VlasnikService } from '../vlasnik.service';
import { KorisnikLoginResponse } from '../../responses/KorisnikLoginResponse';
import Swal from 'sweetalert2';
import { NovaVikendicaRequest } from '../../requests/NovaVIkendicaRequest';

@Component({
  selector: 'app-moje-vikendice',
  standalone: true,
  imports: [FormsModule, CommonModule, RouterModule],
  templateUrl: './moje-vikendice.component.html',
  styleUrl: './moje-vikendice.component.css'
})
export class MojeVikendiceComponent implements OnInit{
  ngOnInit(): void {
    const korisnikLS = localStorage.getItem('korisnik');
    if (korisnikLS) {
      let k = JSON.parse(korisnikLS);
      this.vlasnikServis.dohvatiKorisnika(k.korisnicko_ime).subscribe(korisnik=>{
        if(korisnik){
          this.vlasnik = korisnik;
          this.ucitajMojeVikendice(this.vlasnik.korisnicko_ime);
        }
      })
    }

  }
  
  vlasnikServis = inject(VlasnikService)
  vikendicaServis = inject(MojeVikendiceService)
  ruter = inject(Router)

  vlasnik: KorisnikLoginResponse = new KorisnikLoginResponse();
  vikendice: Vikendica[] = [];
  novaVikendica: Vikendica = new Vikendica();
  novaUsluga: string = '';
  usluge: string[] = [];
  cenovnik: { sezona: string, cena: number }[] = [];

  ucitajMojeVikendice(vlasnik: string){
    this.vikendicaServis.getMojeVikendice(vlasnik).subscribe(vik=>{
      this.vikendice = vik;
    })
  }

  uredi(id: number) {
    this.ruter.navigate([`/vlasnik/moje-vikendice/${id}`]);
  }
  obrisi(id: number) {
    Swal.fire({
      title: 'Da li ste sigurni?',
      text: 'Ova akcija će trajno obrisati vikendicu.',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Da, obriši',
      cancelButtonText: 'Ne, otkaži',
      confirmButtonColor: '#d33',
      cancelButtonColor: '#72522bff'
    }).then((result) => {
      if (result.isConfirmed) {
        this.vikendicaServis.obrisiVikendicu(id).subscribe(res => {
          if (res.uspeh) {
            this.ucitajMojeVikendice(this.vlasnik.korisnicko_ime);
            Swal.fire({
              title: 'Uspeh!',
              text: res.poruka,
              icon: 'success',
              confirmButtonText: 'U redu',
              confirmButtonColor: '#72522bff'
            });
          } else {
            Swal.fire({
              title: 'Greška!',
              text: res.poruka,
              icon: 'error',
              confirmButtonText: 'Zatvori',
              confirmButtonColor: '#72522bff'
            });
          }
        });
      }
    });
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

  dodajCenovnik() {
    this.cenovnik.push({ sezona: '', cena: 0 });
  }

  ukloniCenovnik(index: number) {
    this.cenovnik.splice(index, 1);
  }

  dodajVikendicu() {
    this.novaVikendica.vlasnik = this.vlasnik.korisnicko_ime;
    this.novaVikendica.usluge = this.usluge.join(',');

    let vik: NovaVikendicaRequest = {
      vikendica: this.novaVikendica,
      cenovnik: this.cenovnik,
      slike: this.slike
    };

    this.vikendicaServis.dodajVikendicuSaCenovnikom(vik).subscribe(res=>{
      if(res.uspeh){
        Swal.fire({
          title: 'Uspeh!',
          text: 'Vikendica i cenovnik su uspešno dodati.',
          icon: 'success',
          confirmButtonText: 'U redu',
          confirmButtonColor: '#72522bff'
        });
        //this.resetForma();
        this.ucitajMojeVikendice(this.vlasnik.korisnicko_ime);
      }else{
        Swal.fire({
          title: 'Greška!',
          text: res.poruka,
          icon: 'error',
          confirmButtonText: 'Zatvori',
          confirmButtonColor: '#72522bff'
        });
      }

    });
  }

  ucitajJSON(event: Event) {
    const input = event.target as HTMLInputElement;
    if (!input.files || input.files.length === 0) {
      return;
    }

    const file = input.files[0];
    const reader = new FileReader();

    reader.onload = () => {
      try {
        const data = JSON.parse(reader.result as string);

        // Popuni formu sa podacima iz JSON-a
        this.novaVikendica.naziv = data.naziv || '';
        this.novaVikendica.mesto = data.mesto || '';
        this.novaVikendica.telefon = data.telefon || '';
        this.novaVikendica.lat = data.lat || 0;
        this.novaVikendica.lon = data.lon || 0;
        this.novaVikendica.blokirana_do = data.blokirana_do || null;

        // Usluge iz JSON-a (ako su string -> split, ako su niz -> direktno)
        if (Array.isArray(data.usluge)) {
          this.usluge = data.usluge;
        } else if (typeof data.usluge === 'string') {
          this.usluge = data.usluge.split(',').map((u: string) => u.trim());
        }

        // Cenovnik iz JSON-a
        if (Array.isArray(data.cenovnik)) {
          this.cenovnik = data.cenovnik.map((stavka: any) => ({
            sezona: stavka.sezona || '',
            cena: stavka.cena || 0
          }));
        } else {
          this.cenovnik = [];
        }

      } catch (err) {
        Swal.fire({
          title: 'Greška!',
          text: 'JSON fajl nije u ispravnom formatu.',
          icon: 'error',
          confirmButtonText: 'U redu'
        });
      }
    };

  reader.readAsText(file);
}

  slike: string[] = []; 
  slikePreview: string[] = []; 

  onSlikeSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (!input.files) return;

    this.slike = [];
    this.slikePreview = [];

    Array.from(input.files).forEach(file => {
      const reader = new FileReader();
      reader.onload = (e: any) => {
        let slikaURL = e.target.result as string;
        this.slike.push(slikaURL); 
        this.slikePreview.push(slikaURL); 
      };
      reader.readAsDataURL(file);
    });
    console.log(this.slike);
  }



}
