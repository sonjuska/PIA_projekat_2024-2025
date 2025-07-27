package com.example.backend.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.backend.db.DB;

public class StatistikaRepo {

    public int ukupanBrojVikendica() {
        String sql = "SELECT COUNT(*) FROM vikendica";
        return izvrsiBrojanje(sql);
    }

    public int ukupanBrojVlasnika() {
        String sql = "SELECT COUNT(*) FROM korisnik WHERE uloga = 'vlasnik' AND aktivan = 1";
        return izvrsiBrojanje(sql);
    }

    public int ukupanBrojTurista() {
        String sql = "SELECT COUNT(*) FROM korisnik WHERE uloga = 'turista' AND aktivan = 1";
        return izvrsiBrojanje(sql);
    }

    public int brojRezervacija24h() {
        String sql = "SELECT COUNT(*) FROM rezervacija WHERE datum_rezervacije >= NOW() - INTERVAL 1 DAY";
        return izvrsiBrojanje(sql);
    }

    public int brojRezervacija7dana() {
        String sql = "SELECT COUNT(*) FROM rezervacija WHERE datum_rezervacije >= NOW() - INTERVAL 7 DAY";
        return izvrsiBrojanje(sql);
    }

    public int brojRezervacija30dana() {
        String sql = "SELECT COUNT(*) FROM rezervacija WHERE datum_rezervacije >= NOW() - INTERVAL 30 DAY";
        return izvrsiBrojanje(sql);
    }

    private int izvrsiBrojanje(String sql) {
        try (Connection conn = DB.source().getConnection();
             PreparedStatement stm = conn.prepareStatement(sql);
             ResultSet rs = stm.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
