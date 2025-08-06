package com.example.backend.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.example.backend.db.DB;
import com.example.backend.modeli.responses.ChartData;
import com.example.backend.modeli.responses.ChartSeries;
import com.example.backend.modeli.responses.PieData;

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

    //dijagrami
    public List<ChartData> dohvatiPodatkeZaDijagramKolona(String korisnicko_ime) {
        List<ChartData> rezultat = new ArrayList<>();

        String sql = """
            SELECT v.id AS vikendica_id, v.naziv,
                MONTH(r.datum_od) AS mesec,
                COUNT(*) AS broj_rezervacija
            FROM vikendica v
            LEFT JOIN rezervacija r ON v.id = r.vikendica_id AND r.status = 'odobrena'
            WHERE v.vlasnik = ?
            GROUP BY v.id, v.naziv, MONTH(r.datum_od)
            ORDER BY mesec, v.id
        """;

        try (Connection conn = DB.source().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, korisnicko_ime);
            ResultSet rs = stmt.executeQuery();

            //sve vikendice
            Map<Integer, String> sveVikendice = new LinkedHashMap<>();
            try (PreparedStatement stmtV = conn.prepareStatement("SELECT id, naziv FROM vikendica WHERE vlasnik = ?")) {
                stmtV.setString(1, korisnicko_ime);
                ResultSet rsV = stmtV.executeQuery();
                while (rsV.next()) {
                    sveVikendice.put(rsV.getInt("id"), rsV.getString("naziv"));
                }
            }

            //mapa mesec-vikendica
            Map<Integer, ChartData> meseciMapa = new LinkedHashMap<>();
            for (int m = 1; m <= 12; m++) {
                ChartData cd = new ChartData();
                cd.setName(getNazivMeseca(m));
                List<ChartSeries> series = new ArrayList<>();
                for (String vikNaziv : sveVikendice.values()) {
                    series.add(new ChartSeries(vikNaziv, 0));
                }
                cd.setSeries(series);
                meseciMapa.put(m, cd);
            }

            
            while (rs.next()) {
                int mesec = rs.getInt("mesec");
                int vikendicaId = rs.getInt("vikendica_id");
                int broj = rs.getInt("broj_rezervacija");

                ChartData mesecData = meseciMapa.get(mesec);
                if (mesecData != null) {
                    for (ChartSeries s : mesecData.getSeries()) {
                        if (s.getName().equals(sveVikendice.get(vikendicaId))) {
                            s.setValue(broj);
                            break;
                        }
                    }
                }
            }

            rezultat.addAll(meseciMapa.values());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rezultat;
    }

    private String getNazivMeseca(int mesec) {
        String[] meseci = {
            "Januar", "Februar", "Mart", "April", "Maj", "Jun",
            "Jul", "Avgust", "Septembar", "Oktobar", "Novembar", "Decembar"
        };
        if (mesec >= 1 && mesec <= 12) {
            return meseci[mesec - 1];
        }
        return "";
    }
    public List<PieData> dohvatiPodatkeZaDijagramPita(String korisnicko_ime) {
        String sql = """
            SELECT v.id AS vikendica_id, v.naziv AS name,
                SUM(CASE WHEN WEEKDAY(r.datum_od) >= 5 THEN 1 ELSE 0 END) AS broj_vikend,
                SUM(CASE WHEN WEEKDAY(r.datum_od) < 5 THEN 1 ELSE 0 END) AS broj_radni_dan
            FROM vikendica v
            LEFT JOIN rezervacija r
            ON v.id = r.vikendica_id AND r.status = 'odobrena'
            WHERE v.vlasnik = ?
            GROUP BY v.id, v.naziv
            ORDER BY v.naziv
        """;

        List<PieData> rezultat = new ArrayList<>();
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, korisnicko_ime);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                PieData pd = new PieData();
                pd.setName(rs.getString("name"));
                pd.setBrojRezervacijaVikend(rs.getInt("broj_vikend"));
                pd.setBrojRezervacijaRadniDani(rs.getInt("broj_radni_dan"));
                rezultat.add(pd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rezultat;
    }





}
