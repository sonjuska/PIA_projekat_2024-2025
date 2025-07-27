package com.example.backend.kontroleri;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.db.dao.VikendicaRepo;
import com.example.backend.modeli.Vikendica;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/vikendice")
public class VikendicaController {

    private final VikendicaRepo repo = new VikendicaRepo();

    @GetMapping("/sve")
    public List<Vikendica> getSveVikendice() {
        return repo.getSveVikendice();
    }

    @GetMapping("/pretraga")
    public List<Vikendica> pretraziVikendice(
        @RequestParam(required = false) String naziv,
        @RequestParam(required = false) String mesto
    ) {
        return repo.pretraziVikendice(naziv, mesto);
    }
}
