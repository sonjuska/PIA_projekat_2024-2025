package com.example.backend.modeli.requests;

import java.util.List;

import com.example.backend.modeli.Cenovnik;
import com.example.backend.modeli.Vikendica;

public class NovaVikendicaRequest {

    private Vikendica vikendica;
    private List<Cenovnik> cenovnik;
    private List<String> slike;

    public NovaVikendicaRequest(Vikendica vikendica, List<Cenovnik> cenovnik, List<String> slike) {
        this.vikendica = vikendica;
        this.cenovnik = cenovnik;
        this.slike = slike;
    }
    public Vikendica getVikendica() {
        return vikendica;
    }
    public void setVikendica(Vikendica vikendica) {
        this.vikendica = vikendica;
    }
    public List<Cenovnik> getCenovnik() {
        return cenovnik;
    }
    public void setCenovnik(List<Cenovnik> cenovnik) {
        this.cenovnik = cenovnik;
    }
    public List<String> getSlike() {
        return slike;
    }
    public void setSlike(List<String> slike) {
        this.slike = slike;
    }

    

}

