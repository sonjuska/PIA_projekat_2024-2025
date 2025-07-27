import { Routes } from '@angular/router';
import { PocetnaComponent } from './pocetna/pocetna.component';
import { LoginComponent } from './login/login.component';
import { RegistracijaComponent } from './registracija/registracija.component';
import { AdminLoginComponent } from './admin/admin-login/admin-login.component';
import { ZahteviZaRegistracijuComponent } from './admin/zahtevi-za-registraciju/zahtevi-za-registraciju.component';
import { KorisniciComponent } from './admin/korisnici/korisnici.component';
import { adminUlogovanGuard } from './admin/admin-ulogovan.guard';
import { AdminPocetnaComponent } from './admin/admin-pocetna/admin-pocetna.component';
import { ProfilComponent } from './turista/profil/profil.component';
import { VikendiceComponent } from './turista/vikendice/vikendice.component';
import { RezervacijeComponent } from './turista/rezervacije/rezervacije.component';
import { turistaUlogovanGuard } from './turista/turista-ulogovan.guard';
import { TuristaPocetnaComponent } from './turista/turista-pocetna/turista-pocetna.component';

export const routes: Routes = [
    {path: "", component: PocetnaComponent},
    {path: "prijava", component: LoginComponent},
    {path: "registracija", component: RegistracijaComponent},
    { path: "admin/prijava", component: AdminLoginComponent },
    {
        path: "admin",
        component: AdminPocetnaComponent,
        canActivate: [adminUlogovanGuard],
        children: [
            { path: "korisnici", component: KorisniciComponent },
            { path: "zahtevi", component: ZahteviZaRegistracijuComponent }
        ]
    },
    {
        path: "turista",
        component: TuristaPocetnaComponent,
        canActivate: [turistaUlogovanGuard],
        children: [
            { path: "profil", component: ProfilComponent },
            { path: "vikendice", component: VikendiceComponent },
            {path: "rezervacije", component: RezervacijeComponent}

        ]
    },

];
