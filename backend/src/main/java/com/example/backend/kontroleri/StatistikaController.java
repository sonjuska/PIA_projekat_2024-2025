package com.example.backend.kontroleri;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.db.dao.StatistikaRepo;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/statistika")
public class StatistikaController {

    private final StatistikaRepo repo = new StatistikaRepo();

    @GetMapping("/ukupanBrojVikendica")
    public int getUkupanBrojVikendica() {
        return repo.ukupanBrojVikendica();
    }

    @GetMapping("/ukupanBrojVlasnika")
    public int getUkupanBrojVlasnika() {
        return repo.ukupanBrojVlasnika();
    }

    @GetMapping("/ukupanBrojTurista")
    public int getUkupanBrojTurista() {
        return repo.ukupanBrojTurista();
    }

    @GetMapping("/rezervacije24h")
    public int getBrojRezervacija24h() {
        return repo.brojRezervacija24h();
    }

    @GetMapping("/rezervacije7dana")
    public int getBrojRezervacija7dana() {
        return repo.brojRezervacija7dana();
    }

    @GetMapping("/rezervacije30dana")
    public int getBrojRezervacija30dana() {
        return repo.brojRezervacija30dana();
    }
}
