import { MojeVikendiceComponent } from './vlasnik/moje-vikendice/moje-vikendice.component';
import { Routes } from '@angular/router';
import { PocetnaComponent } from './pocetna/pocetna.component';
import { LoginComponent } from './login/login.component';
import { RegistracijaComponent } from './registracija/registracija.component';
import { AdminLoginComponent } from './admin/admin-login/admin-login.component';
import { ZahteviZaRegistracijuComponent } from './admin/zahtevi-za-registraciju/zahtevi-za-registraciju.component';
import { KorisniciComponent } from './admin/korisnici/korisnici.component';
import { adminUlogovanGuard } from './admin/admin-ulogovan.guard';
import { AdminPocetnaComponent } from './admin/admin-pocetna/admin-pocetna.component';
import { ProfilComponent as TuristaProfilComponent } from './turista/profil/profil.component';
import { VikendiceComponent } from './turista/vikendice/vikendice.component';
import { RezervacijeComponent as TuristaRezervacijeComponent} from './turista/rezervacije/rezervacije.component';
import { turistaUlogovanGuard } from './turista/turista-ulogovan.guard';
import { TuristaPocetnaComponent } from './turista/turista-pocetna/turista-pocetna.component';
import { PromenaLozinkeComponent } from './promena-lozinke/promena-lozinke.component';
import { VikendicaDetaljiComponent } from './turista/vikendice/vikendica-detalji/vikendica-detalji.component';
import { ZakazivanjeComponent } from './turista/zakazivanje/zakazivanje.component';
import { VlasnikPocetnaComponent } from './vlasnik/vlasnik-pocetna/vlasnik-pocetna.component';
import { vlasnikUlogovanGuard } from './vlasnik/vlasnik-ulogovan.guard';
import { ProfilComponent as VlasnikProfilComponent} from './vlasnik/profil/profil.component';
import { RezervacijeComponent as VlasnikRezervacijeComponent} from './vlasnik/rezervacije/rezervacije.component';
import { UrediMojuVikendicuComponent } from './vlasnik/moje-vikendice/uredi-moju-vikendicu/uredi-moju-vikendicu.component';
import { VlasnikStatistikaComponent } from './vlasnik/vlasnik-statistika/vlasnik-statistika.component';
import { UrediKorisnikaComponent } from './admin/korisnici/uredi-korisnika/uredi-korisnika.component';

export const routes: Routes = [
    {path: "", component: PocetnaComponent},
    {path: "prijava", component: LoginComponent},
    {path: "registracija", component: RegistracijaComponent},
    {path: "promeniLozinku", component: PromenaLozinkeComponent},
    { path: "admin/prijava", component: AdminLoginComponent },
    {
        path: "admin",
        component: AdminPocetnaComponent,
        canActivate: [adminUlogovanGuard],
        children: [
            { path: "korisnici", component: KorisniciComponent },
            { path: "zahtevi", component: ZahteviZaRegistracijuComponent },
            { path: "korisnici/uredi-korisnika/:id", component: UrediKorisnikaComponent}
        ]
    },
    {
        path: "turista",
        component: TuristaPocetnaComponent,
        canActivate: [turistaUlogovanGuard],
        children: [
            { path: "profil", component: TuristaProfilComponent },
            { path: "vikendice", component: VikendiceComponent },
            { path: "rezervacije", component: TuristaRezervacijeComponent},
            { path: "vikendice/:id", component: VikendicaDetaljiComponent},
            { path: "zakazivanje/:id", component: ZakazivanjeComponent}
        ]
    },
        {
        path: "vlasnik",
        component: VlasnikPocetnaComponent,
        canActivate: [vlasnikUlogovanGuard],
        children: [
            { path: "profil", component: VlasnikProfilComponent },
            { path: "rezervacije", component: VlasnikRezervacijeComponent},
            { path: "moje-vikendice", component: MojeVikendiceComponent},
            { path: "moje-vikendice/:id", component: UrediMojuVikendicuComponent},
            { path: "statistika", component: VlasnikStatistikaComponent},

        ]
    }

];
