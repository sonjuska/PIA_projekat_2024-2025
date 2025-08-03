package com.example.backend.kontroleri;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.db.dao.RezervacijeRepo;
import com.example.backend.modeli.responses.DohvatiRezervacijuResponse;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/vlasnik/rezervacije")
public class RezervacijeVlasnikController {
    @GetMapping("/moje-rezervacije")
    public List<DohvatiRezervacijuResponse> dohvatiRezervacijeZaMojeVikendice(@RequestParam String vlasnik) {
        return new RezervacijeRepo().dohvatiRezervacijeZaMojeVikendice(vlasnik);
    }
}
