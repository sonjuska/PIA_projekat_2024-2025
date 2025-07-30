package com.example.backend.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import com.example.backend.db.DB;

public class ZakazivanjeRepo {

    public double izracunajCenu(int vikendicaId, String datumOd, String datumDo, int brojOdraslih, int brojDece) {
        LocalDate start = LocalDate.parse(datumOd);
        LocalDate end = LocalDate.parse(datumDo);

        Map<String, Double> cenePoSezoni = new HashMap<>();

        String sql = "SELECT sezona, cena FROM cenovnik WHERE vikendica_id = ?";
        try (Connection conn = DB.source().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, vikendicaId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String sezona = rs.getString("sezona");
                double cena = rs.getDouble("cena");
                cenePoSezoni.put(sezona.toLowerCase(), cena);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return 0.0;
        }

        double ukupno = 0.0;
        long ukupnoLjudi = brojOdraslih + brojDece;

        for (LocalDate dan = start; dan.isBefore(end); dan = dan.plusDays(1)) {
            int mesec = dan.getMonthValue();
            String sezona = (mesec >= 5 && mesec <= 8) ? "leto" : "zima";
            double cenaPoOsobi = cenePoSezoni.getOrDefault(sezona, 0.0);
            ukupno += cenaPoOsobi * ukupnoLjudi;
        }

        return ukupno;
    }
}

