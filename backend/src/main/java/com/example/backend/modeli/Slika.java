package com.example.backend.modeli;

public class Slika {
    
    private int id;
    private int vikendica_id;
    private String putanja;
    
    public Slika(int id, int vikendica_id, String putanja) {
        this.id = id;
        this.vikendica_id = vikendica_id;
        this.putanja = putanja;
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

    public void setVikendica_id(int vikendica_id) {
        this.vikendica_id = vikendica_id;
    }

    public String getPutanja() {
        return putanja;
    }

    public void setPutanja(String putanja) {
        this.putanja = putanja;
    }
    
    
}
