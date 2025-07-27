package com.example.backend.kontroleri;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend.db.dao.RegistracijaRepo;
import com.example.backend.modeli.Korisnik;
import com.example.backend.modeli.responses.RegistracijaResponse;
import org.mindrot.jbcrypt.BCrypt;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/registracija")
public class RegistracijaController {


    @Autowired
    private RegistracijaRepo korisnikRepo;

    @PostMapping
    public RegistracijaResponse registrujregistruj(
            @RequestParam String korime,
            @RequestParam String lozinka,
            @RequestParam String ime,
            @RequestParam String prezime,
            @RequestParam String pol,
            @RequestParam String adresa,
            @RequestParam String telefon,
            @RequestParam String email,
            @RequestParam String brojKartice,
            @RequestParam String uloga,
            @RequestParam(required = false) MultipartFile slika
    ) {
        RegistracijaResponse res = new RegistracijaResponse();

        if (korisnikRepo.zauzetoKorisnickoIme(korime)) {
            res.setRegistrovan(false);
            res.setPoruka("Korisničko ime je zauzeto.");
            return res;
        }

        if (korisnikRepo.zauzetEmail(email)) {
            res.setRegistrovan(false);
            res.setPoruka("Email je već u upotrebi.");
            return res;
        }
        if(korisnikRepo.zabranjenoKorisnickoIme(korime)){
            res.setRegistrovan(false);
            res.setPoruka("Korisničko ime je zabranjeno.");
            return res;
        }
        if (korisnikRepo.zabranjenEmail(email)) {
            res.setRegistrovan(false);
            res.setPoruka("Email je zabranjen.");
            return res;
        }

        Korisnik korisnik = new Korisnik();
        korisnik.setKorisnicko_ime(korime);
        korisnik.setLozinka_hash(BCrypt.hashpw(lozinka, BCrypt.gensalt()));
        korisnik.setIme(ime);
        korisnik.setPrezime(prezime);
        korisnik.setPol(pol);
        korisnik.setAdresa(adresa);
        korisnik.setTelefon(telefon);
        korisnik.setEmail(email);
        korisnik.setBroj_kartice(brojKartice);
        korisnik.setUloga(uloga);
        korisnik.setAktivan(false);

        String slikaPutanja = "default.jpg";

        // Apsolutna putanja do static/slike foldera
        String staticDir = Paths.get(System.getProperty("user.dir"), "backend", "src", "main", "resources", "static").toString();


        if (slika != null && !slika.isEmpty()) {
            try {
                String originalFileName = StringUtils.cleanPath(slika.getOriginalFilename());
                Path uploadPath = Paths.get(staticDir);

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Path filePath = uploadPath.resolve(originalFileName);
                slika.transferTo(filePath.toFile());

                // Putanja koja se koristi u <img src="...">
                slikaPutanja = originalFileName;

            } catch (IOException e) {
                e.printStackTrace();
                res.setRegistrovan(false);
                res.setPoruka("Greška pri čuvanju slike.");
                return res;
            }
        }

        korisnik.setProfilna_slika_path(slikaPutanja);
        korisnikRepo.registruj(korisnik);

        res.setRegistrovan(true);
        res.setPoruka("Zahtev za registraciju uspešno kreiran.");
        res.setId(korisnik.getKorisnicko_ime());
        return res;
    }
}
