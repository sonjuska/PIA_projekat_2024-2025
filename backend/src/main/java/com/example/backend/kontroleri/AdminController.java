package com.example.backend.kontroleri;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend.db.dao.AdminRepo;
import com.example.backend.modeli.ZahtevZaRegistraciju;
import com.example.backend.modeli.responses.KorisnikLoginResponse;
import com.example.backend.modeli.responses.SimpleResponse;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("zahtevi")
    public List<ZahtevZaRegistraciju> sviZahteviZaRegistraciju() {
        return new AdminRepo().sviZahteviZaRegistraciju();
    }

    @GetMapping("odobriZahtev")
    public int odobri(@RequestParam int id){
        return new AdminRepo().odobriZahtev(id);
    }

    @GetMapping("odbijZahtev")
    public int odbij(@RequestParam int id, @RequestParam String komentar_odbijanja){
        return new AdminRepo().odbijZahtev(id, komentar_odbijanja);
    }

    @GetMapping("korisnici")
    public List<KorisnikLoginResponse> dohvatiKorisnike() {
        return new AdminRepo().dohvatiKorisnike();
    }

    @GetMapping("deaktivirajKorisnika")
    public SimpleResponse deaktivirajKorisnika(@RequestParam String korisnicko_ime){
        return new AdminRepo().deaktivirajKorisnika(korisnicko_ime);
    }

    @GetMapping("uredi-korisnika/korisnik")
    public KorisnikLoginResponse dohvatiKorisnikaPoKorisnickomImenu(@RequestParam String korisnicko_ime){
        return new AdminRepo().dohvatiKorisnikaPoKorisnickomImenu(korisnicko_ime);
    }

    @PostMapping("/azurirajKorisnika")
    public SimpleResponse azurirajKorisnika(
            @RequestParam String korisnicko_ime,
            @RequestParam String ime,
            @RequestParam String prezime,
            @RequestParam String pol,
            @RequestParam(required = false) String adresa,
            @RequestParam(required = false) String telefon,
            @RequestParam String email,
            @RequestParam String broj_kartice,
            @RequestParam String uloga,
            @RequestParam boolean aktivan,
            @RequestParam boolean slikaUklonjena,
            @RequestPart(required = false) MultipartFile slika) {

        return new AdminRepo().azurirajKorisnika(
            korisnicko_ime, ime, prezime, pol, adresa, telefon, email, broj_kartice,
            uloga, aktivan, slikaUklonjena, slika
        );

 
    }


}