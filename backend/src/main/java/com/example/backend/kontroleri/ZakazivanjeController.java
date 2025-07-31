package com.example.backend.kontroleri;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.db.dao.ZakazivanjeRepo;
import com.example.backend.modeli.requests.RezervacijaRequest;
import com.example.backend.modeli.responses.RezervacijaResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/zakazivanje")
public class ZakazivanjeController {
    
    @GetMapping("/izracunajCenu")
    public double izracunajCenu(@RequestParam int vikendica_id, @RequestParam String datumOd, @RequestParam String datumDo, @RequestParam int brojOdraslih, @RequestParam int brojDece) {
        return new ZakazivanjeRepo().izracunajCenu(vikendica_id,datumOd, datumDo, brojOdraslih, brojDece);
    }

    @PostMapping("/potvrdiZakazivanje")
    public RezervacijaResponse potvrdiZakazivanje(@RequestBody RezervacijaRequest podaci){

        return new ZakazivanjeRepo().rezervisi(podaci.getVikendica_id(), podaci.getTurista(), podaci.getDatum_od(),
            podaci.getVreme_od(), podaci.getDatum_do(), podaci.getVreme_do(), podaci.getBroj_odraslih(), podaci.getBroj_dece(),
            podaci.getBroj_kartice(), podaci.getOpis());
    }

}
    

