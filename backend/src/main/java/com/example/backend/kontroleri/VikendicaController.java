package com.example.backend.kontroleri;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.db.dao.VikendicaRepo;
import com.example.backend.modeli.Cenovnik;
import com.example.backend.modeli.Komentar;
import com.example.backend.modeli.Vikendica;
import com.example.backend.modeli.requests.AzurirajVikendicuRequest;
import com.example.backend.modeli.requests.NovaVikendicaRequest;
import com.example.backend.modeli.responses.VikendicaSimpleResponse;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/vikendice")
public class VikendicaController {

    private final VikendicaRepo repo = new VikendicaRepo();

    @GetMapping("/sve")
    public List<Vikendica> getSveVikendice() {
        return repo.getSveVikendice();
    }

    @GetMapping("/id")
    public Vikendica getVikendicaPoId(@RequestParam int id){
        return repo.getVikendicaPoId(id);
    }

    @GetMapping("/pretraga")
    public List<Vikendica> pretraziVikendice(
        @RequestParam(required = false) String naziv,
        @RequestParam(required = false) String mesto
    ) {
        return repo.pretraziVikendice(naziv, mesto);
    }

    @GetMapping("/id/slike")
    public List<String> getSlikeVikendice(@RequestParam int vikendica_id){
        return repo.getSlikeVikendice(vikendica_id);
    }

    @GetMapping("/id/cenovnik")
    public List<Cenovnik> getCenovnikVikendice(@RequestParam int vikendica_id){
        return repo.getCenovnikVikendice(vikendica_id);
    }

    @GetMapping("/id/komentari")
    public List<Komentar> getKomentarVikendice(@RequestParam int vikendica_id){
        return repo.getKomentarVikendice(vikendica_id);
    }

    //vlasnik
    @GetMapping("/moje-vikendice")
    public List<Vikendica> getMojeVikendice(@RequestParam String vlasnik) {
        return repo.getMojeVikendice(vlasnik);
    }
    @DeleteMapping("/moje-vikendice/{id}")
    public VikendicaSimpleResponse obrisiVikendicu(@PathVariable int id){
        return repo.obrisiVikendicu(id);
    }
    @PutMapping("/azuriraj")
    public VikendicaSimpleResponse azurirajVikendicu(@RequestBody AzurirajVikendicuRequest request){
        return repo.azurirajVikendicu(request);
    }
    @PostMapping("/dodajSaCenovnikom")
    public VikendicaSimpleResponse dodajSaCenovnikom(@RequestBody NovaVikendicaRequest request) {
        return repo.dodajVikendicuSaCenovnikom(request);

    }

}
