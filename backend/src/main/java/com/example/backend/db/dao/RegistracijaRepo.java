package com.example.backend.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.backend.db.DB;
import com.example.backend.modeli.Korisnik;

import org.springframework.stereotype.Repository;

@Repository
public class RegistracijaRepo {

    public boolean zauzetoKorisnickoIme(String korisnickoIme) {
        String query = "SELECT 1 FROM korisnik WHERE korisnicko_ime = ?";
        try (Connection conn = DB.source().getConnection();

            PreparedStatement stm = conn.prepareStatement(query)) {
            stm.setString(1, korisnickoIme);
            ResultSet rs = stm.executeQuery();
            return rs.next(); 

        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
    }
    public boolean zabranjenoKorisnickoIme(String korisnickoIme) {
        String query = "SELECT 1 FROM zabranjeni_korisnici WHERE korisnicko_ime = ?";
        try (Connection conn = DB.source().getConnection();

            PreparedStatement stm = conn.prepareStatement(query)) {
            stm.setString(1, korisnickoIme);
            ResultSet rs = stm.executeQuery();
            return rs.next(); 

        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
    }

    public boolean zauzetEmail(String email) {
        String query = "SELECT 1 FROM korisnik WHERE email = ?";
        try (Connection conn = DB.source().getConnection();
             PreparedStatement stm = conn.prepareStatement(query)) {
            stm.setString(1, email);
            ResultSet rs = stm.executeQuery();
            return rs.next();
            
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
    }
    public boolean zabranjenEmail(String email) {
        String query = "SELECT 1 FROM zabranjeni_korisnici WHERE email = ?";
        try (Connection conn = DB.source().getConnection();
             PreparedStatement stm = conn.prepareStatement(query)) {
            stm.setString(1, email);
            ResultSet rs = stm.executeQuery();
            return rs.next();
            
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
    }

    public void registruj(Korisnik k) {
        String query = "INSERT INTO korisnik (korisnicko_ime, lozinka_hash, ime, prezime, pol, adresa, telefon, email, profilna_slika_path, broj_kartice, uloga, aktivan) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DB.source().getConnection();
             PreparedStatement stm = conn.prepareStatement(query)) {

            stm.setString(1, k.getKorisnicko_ime());
            stm.setString(2, k.getLozinka_hash());
            stm.setString(3, k.getIme());
            stm.setString(4, k.getPrezime());
            stm.setString(5, k.getPol());
            stm.setString(6, k.getAdresa());
            stm.setString(7, k.getTelefon());
            stm.setString(8, k.getEmail());
            stm.setString(9, k.getProfilna_slika_path());
            stm.setString(10, k.getBroj_kartice());
            stm.setString(11, k.getUloga());
            stm.setBoolean(12, k.isAktivan());

            stm.executeUpdate();
            kreirajZahtevZaRegistraciju(k.getKorisnicko_ime());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void kreirajZahtevZaRegistraciju(String korisnickoIme) {
        String upit = "INSERT INTO zahtev_za_registraciju (korisnicko_ime) VALUES (?)";
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(upit)) {
            stm.setString(1, korisnickoIme);
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
