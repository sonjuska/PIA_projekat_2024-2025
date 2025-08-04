package com.example.backend.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.backend.db.DB;
import com.example.backend.modeli.requests.OdbijRezervacijuRequest;
import com.example.backend.modeli.responses.ArhivaRezervacijaResponse;
import com.example.backend.modeli.responses.DohvatiRezervacijuResponse;
import com.example.backend.modeli.responses.RezervacijaResponse;

public class RezervacijeRepo {

     public List<DohvatiRezervacijuResponse> aktivneRezervacijeZaTuristu(String turista) {

        String sql = """
            SELECT r.id, v.naziv, v.mesto, r.datum_od, r.datum_do, r.broj_odraslih, r.broj_dece,
                r.broj_kartice, r.opis, r.status, r.komentar_odbijanja
            FROM rezervacija r
            JOIN vikendica v ON r.vikendica_id = v.id
            WHERE r.turista = ? AND r.status != 'otkazana' AND r.datum_do > CURRENT_DATE
            ORDER BY r.datum_od DESC
        """;

        List<DohvatiRezervacijuResponse> lista = new ArrayList<>();

        try (Connection conn = DB.source().getConnection();
             PreparedStatement stm = conn.prepareStatement(sql)) {

            stm.setString(1, turista);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                String datumOd = rs.getTimestamp("datum_od").toLocalDateTime().toLocalDate().toString();
                String vremeOd = rs.getTimestamp("datum_od").toLocalDateTime().toLocalTime().toString().substring(0, 5);

                String datumDo = rs.getTimestamp("datum_do").toLocalDateTime().toLocalDate().toString();
                String vremeDo = rs.getTimestamp("datum_do").toLocalDateTime().toLocalTime().toString().substring(0, 5);

                DohvatiRezervacijuResponse r = new DohvatiRezervacijuResponse(
                    rs.getInt("id"),
                    rs.getString("naziv"),
                    rs.getString("mesto"),
                    datumOd,
                    vremeOd,
                    datumDo,
                    vremeDo,
                    rs.getInt("broj_odraslih"),
                    rs.getInt("broj_dece"),
                    rs.getString("broj_kartice"),
                    rs.getString("opis"),
                    rs.getString("status"),
                    rs.getString("komentar_odbijanja")
                );
                lista.add(r);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }


    public List<ArhivaRezervacijaResponse> arhiviraneRezervacijeZaTuristu(String turista) {
        List<ArhivaRezervacijaResponse> rezultat = new ArrayList<>();

        String sql = """
            SELECT a.id, v.naziv, r.datum_od, r.datum_do, r.broj_odraslih, r.broj_dece, r.broj_kartice, 
                   r.opis, a.ocena, a.tekst
            FROM arhiva a
            JOIN rezervacija r ON a.rezervacija_id = r.id
            JOIN vikendica v ON r.vikendica_id = v.id
            WHERE r.turista = ? AND r.status = 'odobrena'
            ORDER BY a.datum DESC
        """;

        try (Connection conn = DB.source().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, turista);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String naziv = rs.getString("naziv");

                LocalDateTime datumOd = rs.getTimestamp("datum_od").toLocalDateTime();
                LocalDateTime datumDo = rs.getTimestamp("datum_do").toLocalDateTime();

                String datum_od = datumOd.toLocalDate().toString();
                String vreme_od = datumOd.toLocalTime().toString().substring(0, 5);

                String datum_do = datumDo.toLocalDate().toString();
                String vreme_do = datumDo.toLocalTime().toString().substring(0, 5);

                int broj_odraslih = rs.getInt("broj_odraslih");
                int broj_dece = rs.getInt("broj_dece");
                String broj_kartice = rs.getString("broj_kartice");
                String opis = rs.getString("opis");
                int ocena = rs.getInt("ocena");
                String tekst = rs.getString("tekst");

                ArhivaRezervacijaResponse arhiva = new ArhivaRezervacijaResponse(id,
                    naziv, datum_od, vreme_od, datum_do, vreme_do,
                        broj_odraslih, broj_dece, broj_kartice, opis, ocena, tekst
                );

                rezultat.add(arhiva);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rezultat;
    }

    public int posaljiKomentar(ArhivaRezervacijaResponse arhiva) {
        String sql = "UPDATE arhiva SET ocena = ?, tekst = ? WHERE id = ?";

        try (Connection conn = DB.source().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, arhiva.getOcena());
            stmt.setString(2, arhiva.getTekst());
            stmt.setLong(3, arhiva.getId());

            return stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public RezervacijaResponse otkaziRezervaciju(int id){
        String sql = "UPDATE rezervacija SET status = 'otkazana' WHERE id = ?";

        try (Connection conn = DB.source().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            return new RezervacijaResponse(stmt.executeUpdate(), "Rezervacija uspešno otkazana.");

        } catch (SQLException e) {
            e.printStackTrace();
            return new RezervacijaResponse(0, "Greška pri otkazivanju.");
        }
    }

    //vlasnik
    public List<DohvatiRezervacijuResponse> dohvatiRezervacijeZaMojeVikendice(String vlasnik){
                String sql = """
                    SELECT r.*, v.*
                    FROM rezervacija r
                    JOIN vikendica v ON r.vikendica_id = v.id
                    JOIN korisnik k ON v.vlasnik = k.korisnicko_ime
                    WHERE k.korisnicko_ime = ? AND r.status!= 'otkazana'
                    ORDER BY r.datum_rezervacije DESC;
                """;

        List<DohvatiRezervacijuResponse> lista = new ArrayList<>();

        try (Connection conn = DB.source().getConnection();
             PreparedStatement stm = conn.prepareStatement(sql)) {

            stm.setString(1, vlasnik);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                String datumOd = rs.getTimestamp("datum_od").toLocalDateTime().toLocalDate().toString();
                String vremeOd = rs.getTimestamp("datum_od").toLocalDateTime().toLocalTime().toString().substring(0, 5);

                String datumDo = rs.getTimestamp("datum_do").toLocalDateTime().toLocalDate().toString();
                String vremeDo = rs.getTimestamp("datum_do").toLocalDateTime().toLocalTime().toString().substring(0, 5);

                DohvatiRezervacijuResponse r = new DohvatiRezervacijuResponse(
                    rs.getInt("id"),
                    rs.getString("naziv"),
                    rs.getString("mesto"),
                    datumOd,
                    vremeOd,
                    datumDo,
                    vremeDo,
                    rs.getInt("broj_odraslih"),
                    rs.getInt("broj_dece"),
                    rs.getString("broj_kartice"),
                    rs.getString("opis"),
                    rs.getString("status"),
                    rs.getString("komentar_odbijanja")
                );
                lista.add(r);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public RezervacijaResponse potvrdiRezervaciju(int id){
        String sql = "UPDATE rezervacija SET status = 'odobrena' WHERE id = ?";

        try (Connection conn = DB.source().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            return new RezervacijaResponse(stmt.executeUpdate(), "Rezervacija uspešno odobrena.");

        } catch (SQLException e) {
            e.printStackTrace();
            return new RezervacijaResponse(0, "Greška pri odobravanju.");
        }
    }
    public RezervacijaResponse odbijRezervaciju(OdbijRezervacijuRequest rez){
        String sql = "UPDATE rezervacija SET status = 'odbijena', komentar_odbijanja = ? WHERE id = ?";

        try (Connection conn = DB.source().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, rez.getKomentar_odbijanja());
            stmt.setInt(2, rez.getId());

            return new RezervacijaResponse(stmt.executeUpdate(), "Rezervacija uspešno odbijena.");

        } catch (SQLException e) {
            e.printStackTrace();
            return new RezervacijaResponse(0, "Greška pri odbijanju.");
        }
    }

}
    

