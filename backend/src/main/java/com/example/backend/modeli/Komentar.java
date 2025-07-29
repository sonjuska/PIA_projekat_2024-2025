package com.example.backend.modeli;

import java.time.LocalDateTime;

public class Komentar {
    private int ocena;
    private String tekst;
    private LocalDateTime datum;
    
    public Komentar(int ocena, String tekst, LocalDateTime datum) {
        this.ocena = ocena;
        this.tekst = tekst;
        this.datum = datum;
    }
    public int getOcena() {
        return ocena;
    }
    public void setOcena(int ocena) {
        this.ocena = ocena;
    }
    public String getTekst() {
        return tekst;
    }
    public void setTekst(String tekst) {
        this.tekst = tekst;
    }
    public LocalDateTime getDatum() {
        return datum;
    }
    public void setDatum(LocalDateTime datum) {
        this.datum = datum;
    }

    
}
