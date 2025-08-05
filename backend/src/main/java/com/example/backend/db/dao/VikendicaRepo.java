package com.example.backend.db.dao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.backend.db.DB;
import com.example.backend.modeli.Cenovnik;
import com.example.backend.modeli.Komentar;
import com.example.backend.modeli.Vikendica;
import com.example.backend.modeli.requests.AzurirajVikendicuRequest;
import com.example.backend.modeli.requests.NovaVikendicaRequest;
import com.example.backend.modeli.responses.VikendicaSimpleResponse;

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

    //vlasnik
    public List<Vikendica> getMojeVikendice(String vlasnik){
        List<Vikendica> vikendice = new ArrayList<>();
        Map<Integer, Double> ocene = ucitajProsecneOcene();

        String sql = "SELECT * FROM vikendica WHERE vlasnik = ?";

        try (Connection conn = DB.source().getConnection();
             PreparedStatement stm = conn.prepareStatement(sql);) {
                stm.setString(1, vlasnik);
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

    public VikendicaSimpleResponse obrisiVikendicu(int id) {
        String obrisiRezervacijeSQL = "DELETE FROM rezervacija WHERE vikendica_id = ?";
        String obrisiVikendicuSQL = "DELETE FROM vikendica WHERE id = ?";
        String obrisiCenovnik = "DELETE FROM cenovnik WHERE vikendica_id = ?";

        try (Connection conn = DB.source().getConnection()) {
            conn.setAutoCommit(false);

            try (
                PreparedStatement stmt1 = conn.prepareStatement(obrisiRezervacijeSQL);
                PreparedStatement stmt2 = conn.prepareStatement(obrisiVikendicuSQL);
                PreparedStatement stmt3 = conn.prepareStatement(obrisiCenovnik);
            ) {
                stmt1.setInt(1, id);
                stmt1.executeUpdate();

                stmt3.setInt(1, id);
                stmt3.executeUpdate();

                stmt2.setInt(1, id);
                int rezultat = stmt2.executeUpdate();

                conn.commit();

                if(rezultat>0) {
                    return new VikendicaSimpleResponse(true, "Vikendica, povezane rezervacije i cenovnik su uspešno obrisane.");
                }else{
                    return new VikendicaSimpleResponse(false, "Vikendica nije pronađena.");
                }

            } catch (SQLException ex) {
                conn.rollback();
                ex.printStackTrace();
                return new VikendicaSimpleResponse(false, "Greška pri brisanju: " + ex.getMessage());
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return new VikendicaSimpleResponse(false, "Greška pri povezivanju sa bazom: " + e.getMessage());
        }
    }

    public VikendicaSimpleResponse azurirajVikendicu(AzurirajVikendicuRequest request) {
        String sql = """
            UPDATE vikendica
            SET naziv = ?, mesto = ?, usluge = ?, telefon = ?, lat = ?, lon = ?, blokirana_do = ?
            WHERE id = ?
        """;

        String sqlDodajSliku = "INSERT INTO slika (vikendica_id, putanja) VALUES (?, ?)";
        String sqlObrisiSliku = "DELETE FROM slika WHERE vikendica_id = ? AND putanja = ?";

        try (Connection conn = DB.source().getConnection()) {
            conn.setAutoCommit(false);

            // 1️⃣ Ažuriranje osnovnih podataka
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, request.getVikendica().getNaziv());
                stmt.setString(2, request.getVikendica().getMesto());
                stmt.setString(3, request.getVikendica().getUsluge());
                stmt.setString(4, request.getVikendica().getTelefon());
                stmt.setDouble(5, request.getVikendica().getLat());
                stmt.setDouble(6, request.getVikendica().getLon());

                if (request.getVikendica().getBlokirana_do() != null) {
                    stmt.setTimestamp(7, java.sql.Timestamp.valueOf(request.getVikendica().getBlokirana_do()));
                } else {
                    stmt.setNull(7, java.sql.Types.TIMESTAMP);
                }

                stmt.setInt(8, request.getVikendica().getId());

                int rezultat = stmt.executeUpdate();
                if (rezultat == 0) {
                    conn.rollback();
                    return new VikendicaSimpleResponse(false, "Vikendica nije pronađena.");
                }
            }

            int vikendicaId = request.getVikendica().getId();

            // Putanja do foldera za slike
            Path folderPath = Paths.get(System.getProperty("user.dir"), "backend", "src", "main", "resources",
                    "static", "vikendice", String.valueOf(vikendicaId));
            if (!Files.exists(folderPath)) {
                Files.createDirectories(folderPath);
            }

            // 2️⃣ Brisanje starih slika
            if (request.getObrisaneSlike() != null) {
                try (PreparedStatement psBrisanje = conn.prepareStatement(sqlObrisiSliku)) {
                    for (String putanja : request.getObrisaneSlike()) {
                        // Obriši fajl iz sistema
                        Path filePath = Paths.get(System.getProperty("user.dir"), "backend", "src", "main", "resources",
                                "static", putanja);
                        Files.deleteIfExists(filePath);

                        // Obriši iz baze
                        psBrisanje.setInt(1, vikendicaId);
                        psBrisanje.setString(2, putanja);
                        psBrisanje.addBatch();
                    }
                    psBrisanje.executeBatch();
                }
            }

            // 3️⃣ Dodavanje novih slika
            if (request.getNoveSlike() != null) {
                try (PreparedStatement psDodaj = conn.prepareStatement(sqlDodajSliku)) {
                    for (String base64 : request.getNoveSlike()) {
                        // generiši ime fajla
                        String fileName = request.getVikendica().getVlasnik() + "_" + System.currentTimeMillis() + ".png";
                        Path filePath = folderPath.resolve(fileName);

                        // dekodiraj i snimi fajl
                        byte[] imageBytes = Base64.getDecoder().decode(base64.split(",")[1]);
                        Files.write(filePath, imageBytes);

                        // putanja koja ide u bazu
                        String relativePath = "vikendice/" + vikendicaId + "/" + fileName;
                        psDodaj.setInt(1, vikendicaId);
                        psDodaj.setString(2, relativePath);
                        psDodaj.addBatch();
                    }
                    psDodaj.executeBatch();
                }
            }

            conn.commit();
            return new VikendicaSimpleResponse(true, "Vikendica je uspešno ažurirana.");

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            return new VikendicaSimpleResponse(false, "Greška pri ažuriranju: " + e.getMessage());
        }
    }


    public VikendicaSimpleResponse dodajVikendicuSaCenovnikom(NovaVikendicaRequest request) {
        String sqlVikendica = """
            INSERT INTO vikendica (vlasnik, naziv, mesto, usluge, telefon, lat, lon, blokirana_do)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        String sqlCenovnik = """
            INSERT INTO cenovnik (vikendica_id, sezona, cena)
            VALUES (?, ?, ?)
        """;
        String sqlSlika = """
            INSERT INTO slika (vikendica_id, putanja)
            VALUES (?, ?)
        """;

        try (Connection conn = DB.source().getConnection()) {
            conn.setAutoCommit(false);

            int vikendicaId;

            // 1️⃣ Dodavanje vikendice
            try (PreparedStatement ps = conn.prepareStatement(sqlVikendica, PreparedStatement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, request.getVikendica().getVlasnik());
                ps.setString(2, request.getVikendica().getNaziv());
                ps.setString(3, request.getVikendica().getMesto());
                ps.setString(4, request.getVikendica().getUsluge());
                ps.setString(5, request.getVikendica().getTelefon());
                ps.setDouble(6, request.getVikendica().getLat());
                ps.setDouble(7, request.getVikendica().getLon());

                if (request.getVikendica().getBlokirana_do() != null) {
                    ps.setTimestamp(8, java.sql.Timestamp.valueOf(request.getVikendica().getBlokirana_do()));
                } else {
                    ps.setNull(8, java.sql.Types.TIMESTAMP);
                }

                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        vikendicaId = rs.getInt(1);
                    } else {
                        conn.rollback();
                        return new VikendicaSimpleResponse(false, "Greška: ID vikendice nije generisan.");
                    }
                }
            }

            try (PreparedStatement psCen = conn.prepareStatement(sqlCenovnik)) {
                for (Cenovnik c : request.getCenovnik()) {
                    psCen.setInt(1, vikendicaId);
                    psCen.setString(2, c.getSezona());
                    psCen.setDouble(3, c.getCena());
                    psCen.addBatch();
                }
                psCen.executeBatch();
            }

        // 3️⃣ Čuvanje slika
        if (request.getSlike() != null && !request.getSlike().isEmpty()) {
            // folder: static/vikendice/{id_vikendice}
            Path folderPath = Paths.get(System.getProperty("user.dir"), "backend", "src", "main", "resources",
                    "static", "vikendice", String.valueOf(vikendicaId));

            if (!Files.exists(folderPath)) {
                try {
                    Files.createDirectories(folderPath);
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }

            try (PreparedStatement psSlika = conn.prepareStatement(sqlSlika)) {
                for (String base64 : request.getSlike()) {
                    // napravi ime fajla vlasnik_timestamp
                    String fileName = request.getVikendica().getVlasnik() + "_" + System.currentTimeMillis() + ".png";
                    Path filePath = folderPath.resolve(fileName);

                    // dekodiraj base64 i snimi fajl
                    byte[] imageBytes = Base64.getDecoder().decode(base64.split(",")[1]);

                    try {
                        Files.write(filePath, imageBytes);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //upiši putanju u bazu (relativna od /static)
                    String relativePath = "vikendice/" + vikendicaId + "/" + fileName;
                    psSlika.setInt(1, vikendicaId);
                    psSlika.setString(2, relativePath);
                    psSlika.addBatch();
                }
                psSlika.executeBatch();
            }
        }

        conn.commit();
        return new VikendicaSimpleResponse(true, "Vikendica, cenovnik i slike uspešno dodati.");


        } catch (SQLException e) {
            e.printStackTrace();
            return new VikendicaSimpleResponse(false, "Greška pri dodavanju: " + e.getMessage());
        }
    }




}
