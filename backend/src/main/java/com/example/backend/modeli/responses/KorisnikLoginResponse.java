package com.example.backend.modeli.responses;

public class KorisnikLoginResponse {
    private String korisnicko_ime;
    private String ime;
    private String prezime;
    private String pol;
    private String adresa;
    private String telefon;
    private String email;
    private String profilna_slika_path;
    private String broj_kartice;
    private String uloga;
    public KorisnikLoginResponse(String korisnicko_ime, String ime, String prezime, String pol, String adresa,
            String telefon, String email, String profilna_slika_path, String broj_kartice, String uloga,
            boolean aktivan) {
        this.korisnicko_ime = korisnicko_ime;
        this.ime = ime;
        this.prezime = prezime;
        this.pol = pol;
        this.adresa = adresa;
        this.telefon = telefon;
        this.email = email;
        this.profilna_slika_path = profilna_slika_path;
        this.broj_kartice = broj_kartice;
        this.uloga = uloga;
        this.aktivan = aktivan;
    }

    private boolean aktivan;

    public String getKorisnicko_ime() {
        return korisnicko_ime;
    }

    public void setKorisnicko_ime(String korisnicko_ime) {
        this.korisnicko_ime = korisnicko_ime;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getPol() {
        return pol;
    }

    public void setPol(String pol) {
        this.pol = pol;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilna_slika_path() {
        return profilna_slika_path;
    }

    public void setProfilna_slika_path(String profilna_slika_path) {
        this.profilna_slika_path = profilna_slika_path;
    }

    public String getBroj_kartice() {
        return broj_kartice;
    }

    public void setBroj_kartice(String broj_kartice) {
        this.broj_kartice = broj_kartice;
    }

    public String getUloga() {
        return uloga;
    }

    public void setUloga(String uloga) {
        this.uloga = uloga;
    }

    public boolean isAktivan() {
        return aktivan;
    }

    public void setAktivan(boolean aktivan) {
        this.aktivan = aktivan;
    }
}
