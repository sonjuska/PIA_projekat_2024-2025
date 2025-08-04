package com.example.backend.kontroleri;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.db.dao.RezervacijeRepo;
import com.example.backend.modeli.requests.OdbijRezervacijuRequest;
import com.example.backend.modeli.responses.DohvatiRezervacijuResponse;
import com.example.backend.modeli.responses.RezervacijaResponse;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/vlasnik/rezervacije")
public class RezervacijeVlasnikController {
    @GetMapping("/moje-rezervacije")
    public List<DohvatiRezervacijuResponse> dohvatiRezervacijeZaMojeVikendice(@RequestParam String vlasnik) {
        return new RezervacijeRepo().dohvatiRezervacijeZaMojeVikendice(vlasnik);
    }
    @PutMapping("/potvrdiRezervaciju/{id}")
    public RezervacijaResponse potvrdiRezervaciju(@PathVariable int id) {
        return new RezervacijeRepo().potvrdiRezervaciju(id);
    }
    @PutMapping("/odbijRezervaciju")
    public RezervacijaResponse odbijRezervaciju(@RequestBody OdbijRezervacijuRequest body) {
        return new RezervacijeRepo().odbijRezervaciju(body);
    }
}
