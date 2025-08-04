package com.example.backend.modeli.requests;

public class OdbijRezervacijuRequest {
    
    private int id;
    private String komentar_odbijanja;
    
    public OdbijRezervacijuRequest(int id, String komentar_odbijanja) {
        this.id = id;
        this.komentar_odbijanja = komentar_odbijanja;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getKomentar_odbijanja() {
        return komentar_odbijanja;
    }
    public void setKomentar_odbijanja(String komentar_odbijanja) {
        this.komentar_odbijanja = komentar_odbijanja;
    }

    
}
