package com.example.backend.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;

import com.example.backend.db.DB;
import com.example.backend.modeli.Korisnik;
import com.example.backend.modeli.responses.KorisnikLoginResponse;

public class LoginRepo {

    public KorisnikLoginResponse loginKorisnik(Korisnik p) {
        try (Connection conn = DB.source().getConnection();
             PreparedStatement stm = conn.prepareStatement(
                "select * from korisnik where korisnicko_ime = ? and uloga in ('turista','vlasnik')")) {

            stm.setString(1, p.getKorisnicko_ime());

            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                String hash = rs.getString("lozinka_hash");

                //provera hesirane lozinke
                if (BCrypt.checkpw(p.getLozinka_hash(), hash)) {
                    KorisnikLoginResponse korisnik = new KorisnikLoginResponse();
                    korisnik.setKorisnicko_ime(rs.getString("korisnicko_ime"));
                    korisnik.setIme(rs.getString("ime"));
                    korisnik.setPrezime(rs.getString("prezime"));
                    korisnik.setPol(rs.getString("pol"));
                    korisnik.setAdresa(rs.getString("adresa"));
                    korisnik.setTelefon(rs.getString("telefon"));
                    korisnik.setEmail(rs.getString("email"));
                    korisnik.setProfilna_slika_path(rs.getString("profilna_slika_path"));
                    korisnik.setBroj_kartice(rs.getString("broj_kartice"));
                    korisnik.setUloga(rs.getString("uloga"));
                    korisnik.setAktivan(rs.getBoolean("aktivan"));
                    return korisnik;
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

        public KorisnikLoginResponse loginAdmin(Korisnik p) {
        try (Connection conn = DB.source().getConnection();
             PreparedStatement stm = conn.prepareStatement(
                 "select * from korisnik where korisnicko_ime = ? and uloga = 'administrator'")) {

            stm.setString(1, p.getKorisnicko_ime());

            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                String hash = rs.getString("lozinka_hash");
                System.out.println(hash);
                //provera hesirane lozinke
                if (BCrypt.checkpw(p.getLozinka_hash(), hash)) {
                    KorisnikLoginResponse korisnik = new KorisnikLoginResponse();
                    korisnik.setKorisnicko_ime(rs.getString("korisnicko_ime"));
                    korisnik.setIme(rs.getString("ime"));
                    korisnik.setPrezime(rs.getString("prezime"));
                    korisnik.setPol(rs.getString("pol"));
                    korisnik.setAdresa(rs.getString("adresa"));
                    korisnik.setTelefon(rs.getString("telefon"));
                    korisnik.setEmail(rs.getString("email"));
                    korisnik.setProfilna_slika_path(rs.getString("profilna_slika_path"));
                    korisnik.setBroj_kartice(rs.getString("broj_kartice"));
                    korisnik.setUloga(rs.getString("uloga"));
                    korisnik.setAktivan(rs.getBoolean("aktivan"));
                    return korisnik;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
