package com.example.backend.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.mindrot.jbcrypt.BCrypt;

import com.example.backend.db.DB;
import com.example.backend.modeli.Korisnik;
import com.example.backend.modeli.responses.KorisnikLoginResponse;

public class LoginRepo {

    public KorisnikLoginResponse loginKorisnik(Korisnik p) {
        try (Connection conn = DB.source().getConnection();
             PreparedStatement stm = conn.prepareStatement(
                "select * from korisnik where korisnicko_ime = ? and uloga in ('turista','vlasnik') and aktivan = 1")) {

            stm.setString(1, p.getKorisnicko_ime());

            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                String hash = rs.getString("lozinka_hash");

                //provera hesirane lozinke
                if (BCrypt.checkpw(p.getLozinka_hash(), hash)) {
                    logujPrijavu(p.getKorisnicko_ime(), true);
                    return new KorisnikLoginResponse(
                        rs.getString("korisnicko_ime"),
                        rs.getString("ime"),
                        rs.getString("prezime"),
                        rs.getString("pol"),
                        rs.getString("adresa"),
                        rs.getString("telefon"),
                        rs.getString("email"),
                        rs.getString("profilna_slika_path"),
                        rs.getString("broj_kartice"),
                        rs.getString("uloga"),
                        rs.getBoolean("aktivan")
                        );
                }
            }
            logujPrijavu(p.getKorisnicko_ime(), false);

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

                //provera hesirane lozinke
                if (BCrypt.checkpw(p.getLozinka_hash(), hash)) {
                    logujPrijavu(p.getKorisnicko_ime(), true);
                    return new KorisnikLoginResponse(
                        rs.getString("korisnicko_ime"),
                        rs.getString("ime"),
                        rs.getString("prezime"),
                        rs.getString("pol"),
                        rs.getString("adresa"),
                        rs.getString("telefon"),
                        rs.getString("email"),
                        rs.getString("profilna_slika_path"),
                        rs.getString("broj_kartice"),
                        rs.getString("uloga"),
                        rs.getBoolean("aktivan")
                        );
                    }
            }
            logujPrijavu(p.getKorisnicko_ime(), false);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void logujPrijavu(String korisnickoIme, boolean uspeh) {
        String query = "INSERT INTO log_prijava (korisnicko_ime, datum_prijave, uspeh) VALUES (?, ?, ?)";
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(query)) {
            
            stm.setString(1, korisnickoIme);
            stm.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            stm.setBoolean(3, uspeh);

            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
}
}
