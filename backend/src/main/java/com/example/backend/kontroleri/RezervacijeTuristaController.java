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
import com.example.backend.modeli.responses.ArhivaRezervacijaResponse;
import com.example.backend.modeli.responses.DohvatiRezervacijuResponse;
import com.example.backend.modeli.responses.RezervacijaResponse;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/turista/rezervacije")
public class RezervacijeTuristaController {
    
    @GetMapping("/aktivne")
    public List<DohvatiRezervacijuResponse> aktivneRezervacijeZaTuristu(@RequestParam String turista) {
        return new RezervacijeRepo().aktivneRezervacijeZaTuristu(turista);
    }
    @GetMapping("/arhivirane")
    public List<ArhivaRezervacijaResponse> arhiviraneRezervacijeZaTuristu(@RequestParam String turista) {
        return new RezervacijeRepo().arhiviraneRezervacijeZaTuristu(turista);
    }
    @PutMapping("/posaljiKomentar")
    public int posaljiKomentar(@RequestBody ArhivaRezervacijaResponse arhiva){
        return new RezervacijeRepo().posaljiKomentar(arhiva);
    }
    @PutMapping("otkaziRezervaciju/{id}")
    public RezervacijaResponse otkaziRezervaciju(@PathVariable int id) {
        return new RezervacijeRepo().otkaziRezervaciju(id);
    }

}
