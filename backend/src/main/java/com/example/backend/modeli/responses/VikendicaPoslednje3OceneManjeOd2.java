package com.example.backend.modeli.responses;

public class VikendicaPoslednje3OceneManjeOd2 {
    private int id;
    private String vlasnik;
    private String naziv;
    private String mesto;
    private String usluge;
    private String telefon;
    private double lat;
    private double lon;
    private String blokirana_do;
    private boolean poslednje3OceneManjeOd2;

    public VikendicaPoslednje3OceneManjeOd2() {
    }

    public VikendicaPoslednje3OceneManjeOd2(int id, String vlasnik, String naziv, String mesto, String usluge,
                                            String telefon, double lat, double lon, String blokirana_do,
                                            boolean poslednje3OceneManjeOd2) {
        this.id = id;
        this.vlasnik = vlasnik;
        this.naziv = naziv;
        this.mesto = mesto;
        this.usluge = usluge;
        this.telefon = telefon;
        this.lat = lat;
        this.lon = lon;
        this.blokirana_do = blokirana_do;
        this.poslednje3OceneManjeOd2 = poslednje3OceneManjeOd2;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVlasnik() {
        return vlasnik;
    }

    public void setVlasnik(String vlasnik) {
        this.vlasnik = vlasnik;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getMesto() {
        return mesto;
    }

    public void setMesto(String mesto) {
        this.mesto = mesto;
    }

    public String getUsluge() {
        return usluge;
    }

    public void setUsluge(String usluge) {
        this.usluge = usluge;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getBlokirana_do() {
        return blokirana_do;
    }

    public void setBlokirana_do(String blokirana_do) {
        this.blokirana_do = blokirana_do;
    }

    public boolean isPoslednje3OceneManjeOd2() {
        return poslednje3OceneManjeOd2;
    }

    public void setPoslednje3OceneManjeOd2(boolean poslednje3OceneManjeOd2) {
        this.poslednje3OceneManjeOd2 = poslednje3OceneManjeOd2;
    }
}
