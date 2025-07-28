package com.example.backend.kontroleri;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.db.dao.PromenaLozinkeRepo;
import com.example.backend.modeli.requests.PromenaLozinkeRequest;
import com.example.backend.modeli.responses.PromenaLozinkeResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/promeniLozinku")
public class PromenaLozinkeController {
    
    @PostMapping()
    public PromenaLozinkeResponse promeniLozinku(@RequestBody PromenaLozinkeRequest body) {
        return new PromenaLozinkeRepo().promeniLozinku(body);
    }
    
}
