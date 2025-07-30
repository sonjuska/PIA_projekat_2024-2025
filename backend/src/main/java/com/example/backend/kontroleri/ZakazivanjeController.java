package com.example.backend.kontroleri;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.db.dao.ZakazivanjeRepo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/zakazivanje")
public class ZakazivanjeController {
    
    @GetMapping("/izracunajCenu")
    public double izracunajCenu(@RequestParam int vikendica_id, @RequestParam String datumOd, @RequestParam String datumDo, @RequestParam int brojOdraslih, @RequestParam int brojDece) {
        return new ZakazivanjeRepo().izracunajCenu(vikendica_id,datumOd, datumDo, brojOdraslih, brojDece);
    }
    
}
