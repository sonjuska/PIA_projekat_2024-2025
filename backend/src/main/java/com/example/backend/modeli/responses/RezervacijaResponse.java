package com.example.backend.modeli.responses;

public class RezervacijaResponse {
    
    private int uspesna;
    private String poruka = "";
    
    public RezervacijaResponse(int uspesna, String poruka) {
        this.uspesna = uspesna;
        this.poruka = poruka;
    }
    public int getUspesna() {
        return uspesna;
    }
    public void setUspesna(int uspesna) {
        this.uspesna = uspesna;
    }
    public String getPoruka() {
        return poruka;
    }
    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }

    
}
