package com.example.backend.modeli.responses;

public class SimpleResponse {
    private Boolean uspeh;
    private String poruka;

    public SimpleResponse(Boolean uspeh, String poruka) {
        this.uspeh = uspeh;
        this.poruka = poruka;
    }
    
    public Boolean getUspeh() {
        return uspeh;
    }
    public void setUspeh(Boolean uspeh) {
        this.uspeh = uspeh;
    }
    public String getPoruka() {
        return poruka;
    }
    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }
}
