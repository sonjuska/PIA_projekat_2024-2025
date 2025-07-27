package com.example.backend.db.dao;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend.db.DB;

@Repository
public class TuristaRepo {

    public int azurirajProfil(String korisnicko_ime, String ime, String prezime,
                                  String adresa, String telefon, String email, String broj_kartice,
                                  MultipartFile slika) {

        String slikaPutanja = null;

        if (slika != null && !slika.isEmpty()) {
            try {
                String staticDir = new File("src/main/resources/static").getAbsolutePath();
                String originalFileName = FilenameUtils.getName(slika.getOriginalFilename());
                Path uploadPath = Paths.get(staticDir);

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Path filePath = uploadPath.resolve(originalFileName);
                slika.transferTo(filePath.toFile());
                slikaPutanja = originalFileName;

            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        }

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
