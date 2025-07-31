package com.example.backend;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;

@Service
@RequiredArgsConstructor
public class ZavrsiRezervacijeServis {

    private final DataSource dataSource;

    @Scheduled(fixedRate = 60000) //svakih 60 sekundi, za produkciju ide 1. dnevno
    public void prebaciRezervacijeUArhivu() {
        String sql = """
            INSERT INTO arhiva (rezervacija_id, ocena, tekst, datum)
            SELECT r.id, NULL, NULL, NOW()
            FROM rezervacija r
            LEFT JOIN arhiva a ON r.id = a.rezervacija_id
            WHERE r.datum_do < NOW() AND a.rezervacija_id IS NULL
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            int ubaceno = stmt.executeUpdate();
            System.out.println("Prebačeno rezervacija u arhivu: " + ubaceno);

        } catch (Exception e) {
            System.err.println("Greška u prebacivanju rezervacija: " + e.getMessage());
        }
    }
}
