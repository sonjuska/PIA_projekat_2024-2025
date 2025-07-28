package com.example.backend.modeli.responses;

public class PromenaLozinkeResponse {
    
    private Boolean promenjena;
    private String poruka;
    private Boolean daLiJeAdmin;

    public PromenaLozinkeResponse(Boolean promenjena, String poruka, Boolean daLiJeAdmin) {
        this.promenjena = promenjena;
        this.poruka = poruka;
        this.daLiJeAdmin = daLiJeAdmin;
    }

    public Boolean getPromenjena() {
        return promenjena;
    }

    public void setPromenjena(Boolean promenjena) {
        this.promenjena = promenjena;
    }

    public String getPoruka() {
        return poruka;
    }

    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }

    public Boolean getDaLiJeAdmin() {
        return daLiJeAdmin;
    }

    public void setDaLiJeAdmin(Boolean daLiJeAdmin) {
        this.daLiJeAdmin = daLiJeAdmin;
    }

    
}
