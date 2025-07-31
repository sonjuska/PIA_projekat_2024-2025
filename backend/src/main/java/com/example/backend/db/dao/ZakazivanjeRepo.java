package com.example.backend.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.example.backend.db.DB;
import com.example.backend.modeli.responses.RezervacijaResponse;

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

    public RezervacijaResponse rezervisi(int vikendica_id, String turista, String datum_od, String vreme_od, String datum_do, String vreme_do,
        int broj_odraslih, int broj_dece, String broj_kartice, String opis){
        String sql = "INSERT INTO rezervacija (vikendica_id, turista, datum_od, datum_do, broj_odraslih, broj_dece, broj_kartice, opis, status, komentar_odbijanja, datum_rezervacije) " +
                 "VALUES (?, ?, ?, ?, ?, ?, ?, ?, DEFAULT, '', CURRENT_TIMESTAMP)";

        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(sql)) {

            // Spajanje datuma i vremena u LocalDateTime
            LocalDateTime datumOd = LocalDateTime.parse(datum_od + "T" + vreme_od);
            LocalDateTime datumDo = LocalDateTime.parse(datum_do + "T" + vreme_do);

            stm.setInt(1, vikendica_id);
            stm.setString(2, turista);
            stm.setTimestamp(3, Timestamp.valueOf(datumOd));
            stm.setTimestamp(4, Timestamp.valueOf(datumDo));
            stm.setInt(5, broj_odraslih);
            stm.setInt(6, broj_dece);
            stm.setString(7, broj_kartice);
            stm.setString(8, opis);

            stm.executeUpdate();
            return new RezervacijaResponse(1, "Rezervacija poslata vlasniku na odobrenje.");

        } catch (Exception e) {
            e.printStackTrace();
            return new RezervacijaResponse(0, "Greška prilikom zakazivanja.");
        }

    }
}

