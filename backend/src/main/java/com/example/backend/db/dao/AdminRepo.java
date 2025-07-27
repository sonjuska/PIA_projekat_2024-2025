package com.example.backend.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.backend.db.DB;
import com.example.backend.modeli.ZahtevZaRegistraciju;

public class AdminRepo {
    
    public List<ZahtevZaRegistraciju> sviZahteviZaRegistraciju(){
        List<ZahtevZaRegistraciju> zahtevi = new ArrayList<>();

        String sql = "SELECT * FROM zahtev_za_registraciju";

        try (Connection conn = DB.source().getConnection();
             PreparedStatement stm = conn.prepareStatement(sql);
             ResultSet rs = stm.executeQuery()) {

            while (rs.next()) {
                ZahtevZaRegistraciju z = new ZahtevZaRegistraciju(
                    rs.getInt("id"),
                    rs.getString("korisnicko_ime"),
                    rs.getString("status"),
                    rs.getString("komentar_odbijanja"),
                    rs.getTimestamp("datum_podnosenja").toLocalDateTime()

                );
                zahtevi.add(z);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return zahtevi;
    }

    public int odobriZahtev(int id){
        String sqlUpdateZahtev = "UPDATE zahtev_za_registraciju SET status = 'odobren' WHERE id = ?";
        String sqlSelectKorime = "SELECT korisnicko_ime FROM zahtev_za_registraciju WHERE id = ?";
        String sqlUpdateKorisnik = "UPDATE korisnik SET aktivan = 1 WHERE korisnicko_ime = ?";

        try (Connection conn = DB.source().getConnection();
            PreparedStatement stmUpdateZahtev = conn.prepareStatement(sqlUpdateZahtev);
            PreparedStatement stmGetKorime = conn.prepareStatement(sqlSelectKorime);
            PreparedStatement stmUpdateKorisnik = conn.prepareStatement(sqlUpdateKorisnik)) {

            //update status zahteva
            stmUpdateZahtev.setInt(1, id);
            int res1 = stmUpdateZahtev.executeUpdate();

            //dohvati korisnicko_ime
            stmGetKorime.setInt(1, id);
            String korisnickoIme = null;
            try (ResultSet rs = stmGetKorime.executeQuery()) {
                if (rs.next()) {
                    korisnickoIme = rs.getString("korisnicko_ime");
                }
            }

            if (korisnickoIme == null) return -2; 

            //aktiviraj korisnika
            stmUpdateKorisnik.setString(1, korisnickoIme);
            int res2 = stmUpdateKorisnik.executeUpdate();

            //uspela oba upita
            return (res1 == 1 && res2 == 1) ? 1 : 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
    
    public int odbijZahtev(int id, String komentar_odbijanja){

        String sql = "UPDATE zahtev_za_registraciju SET status = 'odbijen', komentar_odbijanja = ? where id = ?";
        String sqlSelectKorime = "SELECT korisnicko_ime FROM zahtev_za_registraciju WHERE id = ?";
        String sqlSelectEmail = "SELECT email FROM korisnik WHERE korisnicko_ime = ?";
        String obrisiKorisnika = "DELETE FROM korisnik where korisnicko_ime = ?";
        String insertZabranjen = "INSERT INTO zabranjeni_korisnici(korisnicko_ime, email) VALUES(?,?)";

        String korisnicko_ime = "";
        String email = "";

        try (Connection conn = DB.source().getConnection();
             PreparedStatement stm = conn.prepareStatement(sql);) {
                stm.setString(1, komentar_odbijanja);
                stm.setInt(2, id);
                int res1 = stm.executeUpdate();

                //dohvati korisnicko ime
                PreparedStatement stm2 = conn.prepareStatement(sqlSelectKorime);
                stm2.setInt(1, id);
                ResultSet k = stm2.executeQuery();
                if(k.next()){
                    korisnicko_ime = k.getString("korisnicko_ime");
                }

                //dohvati email
                PreparedStatement stm3 = conn.prepareStatement(sqlSelectEmail);
                stm3.setString(1, korisnicko_ime);
                ResultSet e = stm3.executeQuery();
                if(e.next()){
                    email = e.getString("email");
                }

                //obrisi korinika iz tabele korisnik
                PreparedStatement brisiKorisnika = conn.prepareStatement(obrisiKorisnika);
                brisiKorisnika.setString(1, korisnicko_ime);
                int res2 = brisiKorisnika.executeUpdate();

                //dodaj korisnika u tabelu zabranjeni_korisnici
                PreparedStatement zabranjen = conn.prepareStatement(insertZabranjen);
                zabranjen.setString(1, korisnicko_ime);
                zabranjen.setString(2, email);

                int res3 = zabranjen.executeUpdate();

                return (res1>0 && res2>0 && res3>0? 1 : 0);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
