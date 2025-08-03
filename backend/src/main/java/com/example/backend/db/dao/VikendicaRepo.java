package com.example.backend.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.backend.db.DB;
import com.example.backend.modeli.Cenovnik;
import com.example.backend.modeli.Komentar;
import com.example.backend.modeli.Vikendica;

public class VikendicaRepo {

    public List<Vikendica> getSveVikendice() {
        List<Vikendica> vikendice = new ArrayList<>();
        Map<Integer, Double> ocene = ucitajProsecneOcene();

        String sql = "SELECT * FROM vikendica";

        try (Connection conn = DB.source().getConnection();
             PreparedStatement stm = conn.prepareStatement(sql);
             ResultSet rs = stm.executeQuery()) {

            while (rs.next()) {
                Vikendica v = mapirajVikendicu(rs);
                v.setProsecna_ocena(ocene.getOrDefault(v.getId(), 0.0));
                vikendice.add(v);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vikendice;
    }

    public List<Vikendica> pretraziVikendice(String naziv, String mesto) {
        List<Vikendica> vikendice = new ArrayList<>();
        Map<Integer, Double> ocene = ucitajProsecneOcene();

        StringBuilder sql = new StringBuilder("SELECT * FROM vikendica WHERE 1=1");
        List<Object> parametri = new ArrayList<>();

        if (naziv != null && !naziv.isEmpty()) {
            sql.append(" AND LOWER(naziv) LIKE ?");
            parametri.add("%" + naziv.toLowerCase() + "%");
        }

        if (mesto != null && !mesto.isEmpty()) {
            sql.append(" AND LOWER(mesto) LIKE ?");
            parametri.add("%" + mesto.toLowerCase() + "%");
        }

        try (Connection conn = DB.source().getConnection();
             PreparedStatement stm = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < parametri.size(); i++) {
                stm.setString(i + 1, (String) parametri.get(i));
            }

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                Vikendica v = mapirajVikendicu(rs);
                v.setProsecna_ocena(ocene.getOrDefault(v.getId(), 0.0));
                vikendice.add(v);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vikendice;
    }

    private Vikendica mapirajVikendicu(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String vlasnik = rs.getString("vlasnik");
        String naziv = rs.getString("naziv");
        String mesto = rs.getString("mesto");
        String usluge = rs.getString("usluge");
        String telefon = rs.getString("telefon");
        Double lat = rs.getDouble("lat");
        Double lon = rs.getDouble("lon");

        var blokiranaDo = rs.getTimestamp("blokirana_do");
        LocalDateTime blokirana_do = blokiranaDo != null ? blokiranaDo.toLocalDateTime() : null;

        return new Vikendica(id, vlasnik, naziv, mesto, usluge, telefon, lat, lon, blokirana_do, 0.0);
    }

    private Map<Integer, Double> ucitajProsecneOcene() {
        Map<Integer, Double> prosecneOcene = new HashMap<>();

        String sql = """
            SELECT v.id AS vikendica_id, AVG(a.ocena) AS prosecna_ocena
            FROM vikendica v
            JOIN rezervacija r ON v.id = r.vikendica_id
            JOIN arhiva a ON r.id = a.rezervacija_id
            GROUP BY v.id
        """;

        try (Connection conn = DB.source().getConnection();
             PreparedStatement stm = conn.prepareStatement(sql);
             ResultSet rs = stm.executeQuery()) {

            while (rs.next()) {
                prosecneOcene.put(
                    rs.getInt("vikendica_id"),
                    Math.round(rs.getDouble("prosecna_ocena") * 100.0)/100.0
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return prosecneOcene;
    }

    public Vikendica getVikendicaPoId(int id){
        String sql = "SELECT * FROM vikendica WHERE id = ?";
        Map<Integer, Double> ocene = ucitajProsecneOcene();

        try (Connection conn = DB.source().getConnection();
             PreparedStatement stm = conn.prepareStatement(sql);){
             stm.setInt(1, id);
             ResultSet rs = stm.executeQuery();

            if(rs.next()) {
                Vikendica v = mapirajVikendicu(rs);
                v.setProsecna_ocena(ocene.getOrDefault(v.getId(), 0.0));
                return v;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    public List<String> getSlikeVikendice(int vikendicaId) {
        List<String> putanje = new ArrayList<>();
        
        String sql = "SELECT putanja FROM slika WHERE vikendica_id = ?";

        try (Connection conn = DB.source().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, vikendicaId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                putanje.add(rs.getString("putanja"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return putanje;
    }
        public List<Cenovnik> getCenovnikVikendice(int vikendica_id) {
        List<Cenovnik> cenovnik = new ArrayList<>();
        
        String sql = "SELECT * FROM cenovnik WHERE vikendica_id = ?";

        try (Connection conn = DB.source().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, vikendica_id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
               Cenovnik c = new Cenovnik(
                rs.getInt("id"),
                rs.getInt("vikendica_id"),
                rs.getString("sezona"),
                rs.getDouble("cena")
               );
               cenovnik.add(c);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cenovnik;
    }

    public List<Komentar> getKomentarVikendice(int vikendica_id){
        List<Komentar> komentari = new ArrayList<>();
        
        String sql = """
            SELECT k.ocena, k.tekst, k.datum
            FROM rezervacija r
            JOIN arhiva k ON (r.id = k.rezervacija_id)
            WHERE r.vikendica_id = ?
            AND k.tekst IS NOT NULL
            AND k.tekst != ''
            AND k.ocena IS NOT NULL
            AND k.ocena > 0
        """;

        try (Connection conn = DB.source().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, vikendica_id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
               Komentar c = new Komentar(
                rs.getInt("ocena"),
                rs.getString("tekst"),
                rs.getTimestamp("datum").toLocalDateTime()
               );
               komentari.add(c);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return komentari;
    }

}
