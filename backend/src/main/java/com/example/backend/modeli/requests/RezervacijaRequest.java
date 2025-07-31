package com.example.backend.modeli.requests;

public class RezervacijaRequest {
    
    private int vikendica_id;
    private String turista;
    private String datum_od;
    private String vreme_od;
    private String datum_do;
    private String vreme_do;
    private int broj_odraslih;
    private int broj_dece;
    private String broj_kartice;
    private String opis;

    public RezervacijaRequest(int vikendica_id, String turista, String datum_od, String vreme_od, String datum_do,
                          String vreme_do, int broj_odraslih, int broj_dece, String broj_kartice, String opis) {
        this.vikendica_id = vikendica_id;
        this.turista = turista;
        this.datum_od = datum_od;
        this.vreme_od = vreme_od;
        this.datum_do = datum_do;
        this.vreme_do = vreme_do;
        this.broj_odraslih = broj_odraslih;
        this.broj_dece = broj_dece;
        this.broj_kartice = broj_kartice;
        this.opis = opis;
    }

    public int getVikendica_id() {
        return vikendica_id;
    }

    public void setVikendica_id(int vikendica_id) {
        this.vikendica_id = vikendica_id;
    }

    public String getTurista() {
        return turista;
    }

    public void setTurista(String turista) {
        this.turista = turista;
    }

    public String getDatum_od() {
        return datum_od;
    }

    public void setDatum_od(String datum_od) {
        this.datum_od = datum_od;
    }

    public String getVreme_od() {
        return vreme_od;
    }

    public void setVreme_od(String vreme_od) {
        this.vreme_od = vreme_od;
    }

    public String getDatum_do() {
        return datum_do;
    }

    public void setDatum_do(String datum_do) {
        this.datum_do = datum_do;
    }

    public String getVreme_do() {
        return vreme_do;
    }

    public void setVreme_do(String vreme_do) {
        this.vreme_do = vreme_do;
    }

    public int getBroj_odraslih() {
        return broj_odraslih;
    }

    public void setBroj_odraslih(int broj_odraslih) {
        this.broj_odraslih = broj_odraslih;
    }

    public int getBroj_dece() {
        return broj_dece;
    }

    public void setBroj_dece(int broj_dece) {
        this.broj_dece = broj_dece;
    }

    public String getBroj_kartice() {
        return broj_kartice;
    }

    public void setBroj_kartice(String broj_kartice) {
        this.broj_kartice = broj_kartice;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }
}
