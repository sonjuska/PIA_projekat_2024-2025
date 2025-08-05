package com.example.backend.modeli.requests;

import java.util.List;

import com.example.backend.modeli.Vikendica;

public class AzurirajVikendicuRequest {
    
    private Vikendica vikendica;
    private List<String> noveSlike;
    private List<String> obrisaneSlike;
    
    public AzurirajVikendicuRequest(Vikendica vikendica, List<String> slike, List<String> obrisaneSlike) {
        this.vikendica = vikendica;
        this.noveSlike = slike;
        this.obrisaneSlike = obrisaneSlike;
    }
    public Vikendica getVikendica() {
        return vikendica;
    }
    public void setVikendica(Vikendica vikendica) {
        this.vikendica = vikendica;
    }
    public List<String> getNoveSlike() {
        return noveSlike;
    }
    public void setNoveSlike(List<String> slike) {
        this.noveSlike = slike;
    }
    public List<String> getObrisaneSlike() {
        return obrisaneSlike;
    }
    public void setObrisaneSlike(List<String> obrisaneSlike) {
        this.obrisaneSlike = obrisaneSlike;
    }

    
}
