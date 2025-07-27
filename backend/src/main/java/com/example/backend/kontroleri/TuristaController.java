package com.example.backend.kontroleri;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend.db.dao.TuristaRepo;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/turista")
public class TuristaController {
    
    @PostMapping("/azuriraj")
    public int azurirajProfil(
        @RequestParam String korisnicko_ime,
        @RequestParam String ime,
        @RequestParam String prezime,
        @RequestParam String adresa,
        @RequestParam String telefon,
        @RequestParam String email,
        @RequestParam String broj_kartice,
        @RequestParam(required = false) MultipartFile slika){

        return new TuristaRepo().azurirajProfil(korisnicko_ime, ime, prezime, adresa, telefon, email, broj_kartice, slika);
    }
}
