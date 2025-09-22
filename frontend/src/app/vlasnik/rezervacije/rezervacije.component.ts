import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { VlasnikService } from '../vlasnik.service';
import { DohvatiRezervacijuResponse } from '../../responses/DohvatiRezervacijuResponse';
import { KorisnikLoginResponse } from '../../responses/KorisnikLoginResponse';
import { RezervacijeService } from './rezervacije.service';
import Swal from 'sweetalert2';

//kalendar
import { FullCalendarModule } from '@fullcalendar/angular';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin from '@fullcalendar/interaction';
import { CalendarOptions } from '@fullcalendar/core';
import { srLatnLocale } from '../../latinicaZaKalendar';



@Component({
  selector: 'app-rezervacije',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule, FullCalendarModule],
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
        
            this.calendarOptions.events = this.rezervacije.map(r => ({
              id: String(r.id),
              title: r.naziv,
              start: r.datum_od + 'T' + r.vreme_od,
              end: r.datum_do + 'T' + r.vreme_do,
              allDay: false,
              color: r.status === 'na_cekanju' ? '#fff176' : '#81c784',
              textColor: '#4e342e',
              extendedProps: {
                status: r.status,
                mesto: r.mesto,
                vreme_od: r.vreme_od,
                vreme_do: r.vreme_do
              }

            }
            
          ));
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

  //kalendar
  calendarOptions: CalendarOptions = {
    initialView: 'dayGridMonth',
    plugins: [dayGridPlugin, interactionPlugin],
    locale: srLatnLocale,
    buttonText: {
      today: 'Danas',
      month: 'Mesec',
      week: 'Nedelja',
      day: 'Dan',
      list: 'Lista'
    },
    eventTimeFormat: {
      hour: '2-digit',
      minute: '2-digit',
      hour12: false
    },
    displayEventTime: false,
    events: [],
    eventClick: this.klikNaDogadjaj.bind(this)
  };



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
          this.calendarOptions.events = this.rezervacije.map(r => ({
            title: r.naziv,
            start: r.datum_od + 'T' + r.vreme_od,
            end: r.datum_do + 'T' + r.vreme_do,
            color: r.status === 'na_cekanju' ? '#fff176' : '#81c784',
            textColor: '#4e342e',
            extendedProps: {
              id: r.id,
              status: r.status,
              mesto: r.mesto,
              vreme_od: r.vreme_od,
              vreme_do: r.vreme_do
            }
          }));
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
          this.rezervacijeNaCekanju = [];
          for (let r of this.rezervacije) {
            if (r.status == 'na_cekanju') this.rezervacijeNaCekanju.push(r);
          }
          this.calendarOptions.events = this.rezervacije.map(r => ({
            title: r.naziv,
            start: r.datum_od + 'T' + r.vreme_od,
            end: r.datum_do + 'T' + r.vreme_do,
            color: r.status === 'na_cekanju' ? '#fff176' : '#81c784',
            textColor: '#4e342e',
            extendedProps: {
              id: r.id,
              status: r.status,
              mesto: r.mesto,
              vreme_od: r.vreme_od,
              vreme_do: r.vreme_do
            }
          }));
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

klikNaDogadjaj(info: any) {
  const id = Number(info.event.id ?? info.event.extendedProps['id']);
  const status = info.event.extendedProps['status'];

  if(status !== 'na_cekanju') {
    Swal.fire({
      title: 'Rezervacija već obrađena',
      text: 'Ova rezervacija je već potvrđena.',
      icon: 'info',
      confirmButtonText: 'U redu',
      confirmButtonColor: '#72522bff'
    });
    return;
  }

  Swal.fire({
    title: 'Rezervacija na čekanju',
    html: `
      <textarea id="komentarOdbijanja" class="swal2-textarea" placeholder="Unesite komentar u slučaju odbijanja:" style="width: 80%;"></textarea>
    `,
    showDenyButton: true,
    confirmButtonText: 'Potvrdi rezervaciju',
    denyButtonText: 'Odbij rezervaciju',
    confirmButtonColor: '#4CAF50',
    denyButtonColor: '#f44336',
  }).then(result => {
    
    const komentar = (document.getElementById('komentarOdbijanja') as HTMLTextAreaElement)?.value || '';
    if (result.isConfirmed) {
      this.odobri(id);
    } else if (result.isDenied) {
      if (!komentar.trim()) {
        Swal.fire({
          icon: 'error',
          title: 'Komentar je obavezan.',
          text: 'Morate uneti komentar prilikom odbijanja.',
          confirmButtonColor: '#72522bff'
        });
        return;
      }
      this.odbij(id, komentar);
    }
  });

  }


}
