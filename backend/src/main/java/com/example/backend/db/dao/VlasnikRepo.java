package com.example.backend.db.dao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend.db.DB;
import com.example.backend.modeli.responses.KorisnikLoginResponse;

public class VlasnikRepo {
        public KorisnikLoginResponse dohvatiKorisnika(String korisnicko_ime){
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM korisnik WHERE korisnicko_ime = ?")) {

            stmt.setString(1, korisnicko_ime);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
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
            return null;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    public int azurirajProfil(String korisnicko_ime, String ime, String prezime,
                            String adresa, String telefon, String email, String broj_kartice,
                            boolean slikaUklonjena, MultipartFile slika) {

        String slikaPutanja = null;
        String prethodnaSlika = null;

        // 1. Uzimanje prethodne slike iz baze
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT profilna_slika_path FROM korisnik WHERE korisnicko_ime = ?")) {

            stmt.setString(1, korisnicko_ime);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                prethodnaSlika = rs.getString("profilna_slika_path");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -3;
        }

        // 2. Ako je prosleđena nova slika
        if (slika != null && !slika.isEmpty()) {
            try {
                String staticDir = Paths.get(System.getProperty("user.dir"), "backend", "src", "main", "resources", "static").toString();
                String ekstenzija = FilenameUtils.getExtension(slika.getOriginalFilename());
                String jedinstvenoIme = korisnicko_ime + "_" + System.currentTimeMillis() + "." + ekstenzija;
                Path uploadPath = Paths.get(staticDir);

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Path filePath = uploadPath.resolve(jedinstvenoIme);
                slika.transferTo(filePath.toFile());
                slikaPutanja = jedinstvenoIme;

                // briši staru sliku
                if (prethodnaSlika != null && !prethodnaSlika.equals("default.jpg")) {
                    Files.deleteIfExists(uploadPath.resolve(prethodnaSlika));
                }
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        }

        // 3. Ako je slika uklonjena
        if (slikaUklonjena && (slika == null || slika.isEmpty())) {
            slikaPutanja = "default.jpg";
            // briši staru sliku
            try {
                if (prethodnaSlika != null && !prethodnaSlika.equals("default.jpg")) {
                    String staticDir = Paths.get(System.getProperty("user.dir"), "backend", "src", "main", "resources", "static").toString();
                    Files.deleteIfExists(Paths.get(staticDir).resolve(prethodnaSlika));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 4. UPDATE upit
        String query = "UPDATE korisnik SET ime=?, prezime=?, adresa=?, telefon=?, email=?, broj_kartice=?"
                    + (slikaPutanja != null ? ", profilna_slika_path=?" : "")
                    + " WHERE korisnicko_ime=?";

        try (Connection conn = DB.source().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, ime);
            stmt.setString(2, prezime);
            stmt.setString(3, adresa);
            stmt.setString(4, telefon);
            stmt.setString(5, email);
            stmt.setString(6, broj_kartice);

            int index = 7;
            if (slikaPutanja != null) {
                stmt.setString(index++, slikaPutanja);
            }
            stmt.setString(index, korisnicko_ime);

            return stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return -2;
        }
    }
}
