package com.example.backend.db.dao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend.db.DB;
import com.example.backend.modeli.ZahtevZaRegistraciju;
import com.example.backend.modeli.responses.KorisnikLoginResponse;
import com.example.backend.modeli.responses.SimpleResponse;
import com.example.backend.modeli.responses.VikendicaPoslednje3OceneManjeOd2;

public class AdminRepo {
    
    public List<ZahtevZaRegistraciju> sviZahteviZaRegistraciju(){
        List<ZahtevZaRegistraciju> zahtevi = new ArrayList<>();

        String sql = "SELECT * FROM zahtev_za_registraciju";

        try (Connection conn = DB.source().getConnection();
             PreparedStatement stm = conn.prepareStatement(sql);
             ResultSet rs = stm.executeQuery()) {

            while (rs.next()) {
                ZahtevZaRegistraciju z = new ZahtevZaRegistraciju(
                    rs.getInt("id"),
                    rs.getString("korisnicko_ime"),
                    rs.getString("status"),
                    rs.getString("komentar_odbijanja"),
                    rs.getTimestamp("datum_podnosenja").toLocalDateTime()

                );
                zahtevi.add(z);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return zahtevi;
    }

    public int odobriZahtev(int id){
        String sqlUpdateZahtev = "UPDATE zahtev_za_registraciju SET status = 'odobren' WHERE id = ?";
        String sqlSelectKorime = "SELECT korisnicko_ime FROM zahtev_za_registraciju WHERE id = ?";
        String sqlUpdateKorisnik = "UPDATE korisnik SET aktivan = 1 WHERE korisnicko_ime = ?";

        try (Connection conn = DB.source().getConnection();
            PreparedStatement stmUpdateZahtev = conn.prepareStatement(sqlUpdateZahtev);
            PreparedStatement stmGetKorime = conn.prepareStatement(sqlSelectKorime);
            PreparedStatement stmUpdateKorisnik = conn.prepareStatement(sqlUpdateKorisnik)) {

            //update status zahteva
            stmUpdateZahtev.setInt(1, id);
            int res1 = stmUpdateZahtev.executeUpdate();

            //dohvati korisnicko_ime
            stmGetKorime.setInt(1, id);
            String korisnickoIme = null;
            try (ResultSet rs = stmGetKorime.executeQuery()) {
                if (rs.next()) {
                    korisnickoIme = rs.getString("korisnicko_ime");
                }
            }

            if (korisnickoIme == null) return -2; 

            //aktiviraj korisnika
            stmUpdateKorisnik.setString(1, korisnickoIme);
            int res2 = stmUpdateKorisnik.executeUpdate();

            //uspela oba upita
            return (res1 == 1 && res2 == 1) ? 1 : 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
    
    public int odbijZahtev(int id, String komentar_odbijanja){

        String sql = "UPDATE zahtev_za_registraciju SET status = 'odbijen', komentar_odbijanja = ? where id = ?";
        String sqlSelectKorime = "SELECT korisnicko_ime FROM zahtev_za_registraciju WHERE id = ?";
        String sqlSelectEmail = "SELECT email FROM korisnik WHERE korisnicko_ime = ?";
        String obrisiKorisnika = "DELETE FROM korisnik where korisnicko_ime = ?";
        String insertZabranjen = "INSERT INTO zabranjeni_korisnici(korisnicko_ime, email) VALUES(?,?)";

        String korisnicko_ime = "";
        String email = "";

        try (Connection conn = DB.source().getConnection();
             PreparedStatement stm = conn.prepareStatement(sql);) {
                stm.setString(1, komentar_odbijanja);
                stm.setInt(2, id);
                int res1 = stm.executeUpdate();

                //dohvati korisnicko ime
                PreparedStatement stm2 = conn.prepareStatement(sqlSelectKorime);
                stm2.setInt(1, id);
                ResultSet k = stm2.executeQuery();
                if(k.next()){
                    korisnicko_ime = k.getString("korisnicko_ime");
                }

                //dohvati email
                PreparedStatement stm3 = conn.prepareStatement(sqlSelectEmail);
                stm3.setString(1, korisnicko_ime);
                ResultSet e = stm3.executeQuery();
                if(e.next()){
                    email = e.getString("email");
                }

                //obrisi korinika iz tabele korisnik
                PreparedStatement brisiKorisnika = conn.prepareStatement(obrisiKorisnika);
                brisiKorisnika.setString(1, korisnicko_ime);
                int res2 = brisiKorisnika.executeUpdate();

                //dodaj korisnika u tabelu zabranjeni_korisnici
                PreparedStatement zabranjen = conn.prepareStatement(insertZabranjen);
                zabranjen.setString(1, korisnicko_ime);
                zabranjen.setString(2, email);

                int res3 = zabranjen.executeUpdate();

                return (res1>0 && res2>0 && res3>0? 1 : 0);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public List<KorisnikLoginResponse> dohvatiKorisnike(){
        List<KorisnikLoginResponse> korisnici = new ArrayList<>();

        String sql = "SELECT * FROM korisnik";

        try (Connection conn = DB.source().getConnection();
             PreparedStatement stm = conn.prepareStatement(sql);
             ResultSet rs = stm.executeQuery()) {

            while (rs.next()) {
                KorisnikLoginResponse k = new KorisnikLoginResponse(
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
                korisnici.add(k);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return korisnici;
    }

    public SimpleResponse deaktivirajKorisnika(String korisnicko_ime) {

        String sql = "update korisnik set aktivan = 0  where korisnicko_ime=?";

        try (Connection conn = DB.source().getConnection();
             PreparedStatement stm = conn.prepareStatement(sql);) {

                stm.setString(1, korisnicko_ime);
                int rez = stm.executeUpdate();
            if(rez>0) {
                return new SimpleResponse(true, "Korisnik je uspešno deaktiviran.");
            }else{
                return new SimpleResponse(false, "Neuspešna deaktivacija korisnika.");
            }

        } catch (SQLException e) {
            return new SimpleResponse(false, "Greška pri deaktivaciji korisnika.");

        }

    }

    public KorisnikLoginResponse dohvatiKorisnikaPoKorisnickomImenu(String korisnicko_ime) {

        String sql = "SELECT * FROM korisnik WHERE korisnicko_ime = ?";

        try (Connection conn = DB.source().getConnection();
             PreparedStatement stm = conn.prepareStatement(sql)) {
                stm.setString(1, korisnicko_ime);
                ResultSet rs = stm.executeQuery();

            if(rs.next()) {
                KorisnikLoginResponse k = new KorisnikLoginResponse(
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
                return k;
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public SimpleResponse azurirajKorisnika(String korisnicko_ime, String ime, String prezime, String pol, String adresa,
                                            String telefon, String email, String broj_kartice, String uloga, boolean aktivan,
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
            return new SimpleResponse(false, "Greška pri čitanju prethodne slike iz baze.");
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
                return new SimpleResponse(false, "Greška pri čuvanju nove slike.");
            }
        }

        // 3. Ako je slika uklonjena
        if (slikaUklonjena && (slika == null || slika.isEmpty())) {
            slikaPutanja = "default.jpg";
            try {
                if (prethodnaSlika != null && !prethodnaSlika.equals("default.jpg")) {
                    String staticDir = Paths.get(System.getProperty("user.dir"), "backend", "src", "main", "resources", "static").toString();
                    Files.deleteIfExists(Paths.get(staticDir).resolve(prethodnaSlika));
                }
            } catch (IOException e) {
                e.printStackTrace();
                // Ne prekidamo u ovom slučaju
            }
        }

        // 4. UPDATE upit
        String query = "UPDATE korisnik SET ime=?, prezime=?, pol=?, adresa=?, telefon=?, email=?, broj_kartice=?, uloga=?, aktivan=?"
                    + (slikaPutanja != null ? ", profilna_slika_path=?" : "")
                    + " WHERE korisnicko_ime=?";

        try (Connection conn = DB.source().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, ime);
            stmt.setString(2, prezime);
            stmt.setString(3, pol);
            stmt.setString(4, adresa);
            stmt.setString(5, telefon);
            stmt.setString(6, email);
            stmt.setString(7, broj_kartice);
            stmt.setString(8, uloga);
            stmt.setBoolean(9, aktivan);

            int index = 10;
            if (slikaPutanja != null) {
                stmt.setString(index++, slikaPutanja);
            }

            stmt.setString(index, korisnicko_ime);

            int rez = stmt.executeUpdate();
            if (rez > 0) {
                return new SimpleResponse(true, "Korisnik je uspešno ažuriran.");
            } else {
                return new SimpleResponse(false, "Nijedan korisnik nije ažuriran.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return new SimpleResponse(false, "Greška pri ažuriranju korisnika.");
        }
    }

    public List<VikendicaPoslednje3OceneManjeOd2> dohvatiVikendice(){

        List<VikendicaPoslednje3OceneManjeOd2> vikendice = new ArrayList<>();
        String sql = "SELECT * FROM vikendica";

        try (Connection conn = DB.source().getConnection();
             PreparedStatement stm = conn.prepareStatement(sql);
             ResultSet rs = stm.executeQuery()){;

                while (rs.next()) {
                    int id = rs.getInt("id");
                    boolean posl3OceneManjeOd2 = false;
                    String oceneSQL = """
                            SELECT A.ocena
                            FROM arhiva A JOIN rezervacija R ON (A.rezervacija_id = R.id) JOIN vikendica V ON (V.id = R.vikendica_id)
                            WHERE V.id = ?
                            ORDER BY A.datum DESC
                            LIMIT 3
                            """;
                    PreparedStatement stm2 = conn.prepareStatement(oceneSQL);
                    stm2.setInt(1, id);
                    ResultSet rs2 = stm2.executeQuery();
                    while(rs2.next()){
                        if(rs2.getInt("ocena")>2){
                            posl3OceneManjeOd2 = false;
                            break;
                        }else{
                            posl3OceneManjeOd2 = true;
                        }
                    }
                    VikendicaPoslednje3OceneManjeOd2 k = new VikendicaPoslednje3OceneManjeOd2(
                        id,
                        rs.getString("vlasnik"),
                        rs.getString("naziv"),
                        rs.getString("mesto"),
                        rs.getString("usluge"),
                        rs.getString("telefon"),
                        rs.getDouble("lat"),
                        rs.getDouble("lon"),
                        rs.getString("blokirana_do"),
                        posl3OceneManjeOd2
                    );   

                    vikendice.add(k);
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vikendice;
    }

    public SimpleResponse blokirajVikendicu(int id) {

        try (Connection conn = DB.source().getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE vikendica SET blokirana_do = DATE_ADD(NOW(), INTERVAL 2 day) WHERE id = ?")) {

            stmt.setInt(1, id);
            int res = stmt.executeUpdate();
            if (res>0) {
                return new SimpleResponse(true, "Vikendica je blokirana narednih 48 sati.");
            }
            return new SimpleResponse(false, "Greška pri blokiranju vikendice.");

        } catch (SQLException e) {
            e.printStackTrace();
            return new SimpleResponse(false, "Greška pri blokiranju vikendice.");
        }
    }



}
