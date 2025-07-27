package com.example.backend.kontroleri;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.db.dao.LoginRepo;
import com.example.backend.modeli.Korisnik;
import com.example.backend.modeli.responses.KorisnikLoginResponse;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class LoginController {

    @PostMapping("/login")
    public KorisnikLoginResponse login(@RequestBody Korisnik entity) {
        return new LoginRepo().loginKorisnik(entity);
    }
    @PostMapping("/adminLogin")
    public KorisnikLoginResponse loginAdmin(@RequestBody Korisnik entity) {
        return new LoginRepo().loginAdmin(entity);
    }
}