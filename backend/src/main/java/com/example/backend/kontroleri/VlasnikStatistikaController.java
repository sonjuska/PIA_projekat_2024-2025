package com.example.backend.kontroleri;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.db.dao.StatistikaRepo;
import com.example.backend.modeli.responses.ChartData;
import com.example.backend.modeli.responses.PieData;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/vlasnik-statistika")
public class VlasnikStatistikaController {
    
    @GetMapping("/dijagramKolona")
    public List<ChartData> dohvatiPodatkeZaDijagramKolona(@RequestParam String korisnicko_ime) {
        return new StatistikaRepo().dohvatiPodatkeZaDijagramKolona(korisnicko_ime);
    }
    @GetMapping("/dijagramPita")
    public List<PieData> dohvatiPodatkeZaDijagramPita(@RequestParam String korisnicko_ime) {
        return new StatistikaRepo().dohvatiPodatkeZaDijagramPita(korisnicko_ime);
    }
    
    
}
