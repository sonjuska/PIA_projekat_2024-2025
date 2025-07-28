package com.example.backend.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;

import com.example.backend.db.DB;
import com.example.backend.modeli.requests.PromenaLozinkeRequest;
import com.example.backend.modeli.responses.PromenaLozinkeResponse;

public class PromenaLozinkeRepo {

    public PromenaLozinkeResponse promeniLozinku(PromenaLozinkeRequest p) {
        String sql = "SELECT * FROM korisnik WHERE korisnicko_ime = ?";
        try (Connection conn = DB.source().getConnection();
             PreparedStatement stm = conn.prepareStatement(sql)) {
    
            stm.setString(1, p.getKorisnicko_ime());
            ResultSet rs = stm.executeQuery();
    
            if (!rs.next()) {
                return new PromenaLozinkeResponse(false, "Korisničko ime ne postoji.", false);
            }
    
            String hash = rs.getString("lozinka_hash");
    
            //provera stare lozinke
            if (!BCrypt.checkpw(p.getStaraLozinka(), hash)) {
                return new PromenaLozinkeResponse(false, "Pogrešna stara lozinka.", false);
            }
    
            Boolean daLiJeAdmin = rs.getString("uloga").equals("administrator");
            String novaLozinkaHash = BCrypt.hashpw(p.getNovaLozinka(), BCrypt.gensalt());
            String update = "UPDATE korisnik SET lozinka_hash = ? WHERE korisnicko_ime = ?";

            try (PreparedStatement stm2 = conn.prepareStatement(update)) {
                stm2.setString(1, novaLozinkaHash);
                stm2.setString(2, p.getKorisnicko_ime());
                stm2.executeUpdate();
            }

    
            return new PromenaLozinkeResponse(true, "Lozinka je uspešno promenjena.", daLiJeAdmin);
    
        } catch (SQLException e) {
            e.printStackTrace();
            return new PromenaLozinkeResponse(false, "Greška prilikom promene lozinke.", false);
        }
    }
    
    
}
