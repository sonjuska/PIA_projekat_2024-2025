package com.example.backend.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.backend.db.DB;
import com.example.backend.modeli.Vikendica;

public class VikendicaRepo {

    public List<Vikendica> getSveVikendice() {
        List<Vikendica> vikendice = new ArrayList<>();

        String sql = "SELECT * FROM vikendica";

        try (Connection conn = DB.source().getConnection();
             PreparedStatement stm = conn.prepareStatement(sql);
             ResultSet rs = stm.executeQuery()) {

            while (rs.next()) {
                vikendice.add(mapirajVikendicu(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vikendice;
    }

    public List<Vikendica> pretraziVikendice(String naziv, String mesto) {
        List<Vikendica> vikendice = new ArrayList<>();

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
                vikendice.add(mapirajVikendicu(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vikendice;
    }

    private Vikendica mapirajVikendicu(ResultSet rs) throws SQLException {
        Vikendica v = new Vikendica();
        v.setId(rs.getInt("id"));
        v.setVlasnik(rs.getString("vlasnik"));
        v.setNaziv(rs.getString("naziv"));
        v.setMesto(rs.getString("mesto"));
        v.setUsluge(rs.getString("usluge"));
        v.setTelefon(rs.getString("telefon"));
        v.setLat(rs.getDouble("lat"));
        v.setLon(rs.getDouble("lon"));

        var blokiranaDo = rs.getTimestamp("blokirana_do");
        if (blokiranaDo != null) {
            v.setBlokirana_do(blokiranaDo.toLocalDateTime());
        } else {
            v.setBlokirana_do(null);
        }

        return v;
    }
}
