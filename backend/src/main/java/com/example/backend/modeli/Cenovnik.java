package com.example.backend.modeli;

public class Cenovnik {
    
    private int id;
    private int vikendica_id;
    private String sezona;
    private double cena;
    
    public Cenovnik(int id, int vikendicaId, String sezona, double cena) {
        this.id = id;
        this.vikendica_id = vikendicaId;
        this.sezona = sezona;
        this.cena = cena;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVikendica_id() {
        return vikendica_id;
    }

    public void setVikendica_id(int vikendicaId) {
        this.vikendica_id = vikendicaId;
    }

    public String getSezona() {
        return sezona;
    }

    public void setSezona(String sezona) {
        this.sezona = sezona;
    }

    public double getCena() {
        return cena;
    }

    public void setCena(double cena) {
        this.cena = cena;
    }

    
}
