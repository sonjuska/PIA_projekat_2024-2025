package com.example.backend.modeli.responses;

public class RegistracijaResponse {
    private boolean registrovan;
    private String poruka;
    private String id;

    public boolean isRegistrovan() {
        return registrovan;
    }

    public void setRegistrovan(boolean registrovan) {
        this.registrovan = registrovan;
    }

    public String getPoruka() {
        return poruka;
    }

    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
