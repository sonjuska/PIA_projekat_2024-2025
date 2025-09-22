-- Kreiranje baze ako ne postoji
CREATE DATABASE IF NOT EXISTS PlaninskaVikendica;
USE PlaninskaVikendica;

-- Tabela za korisnike
CREATE TABLE korisnik (
    korisnicko_ime VARCHAR(50) PRIMARY KEY,
    lozinka_hash VARCHAR(255) NOT NULL,
    ime VARCHAR(50) NOT NULL,
    prezime VARCHAR(50) NOT NULL,
    pol CHAR(1) CHECK (pol IN ('M', 'Z')),
    adresa VARCHAR(100),
    telefon VARCHAR(20),
    email VARCHAR(100) UNIQUE,
    profilna_slika_path VARCHAR(255),
    broj_kartice VARCHAR(20),
    uloga ENUM('turista', 'vlasnik', 'administrator') NOT NULL,
    aktivan TINYINT(1) DEFAULT 0
);

-- Tabela za zabranu ponovnog korišćenja odbijenih korisničkih imena i emailova
CREATE TABLE zabranjeni_korisnici (
    korisnicko_ime VARCHAR(50) UNIQUE,
    email VARCHAR(100) UNIQUE
);

-- Tabela za zahteve za registraciju
CREATE TABLE zahtev_za_registraciju (
    id INT AUTO_INCREMENT PRIMARY KEY,
    korisnicko_ime VARCHAR(50),
    status VARCHAR(20) DEFAULT 'na_cekanju' CHECK (status IN ('na_cekanju', 'odobren', 'odbijen')),
    komentar_odbijanja TEXT,
    datum_podnosenja DATETIME DEFAULT NOW()
);

-- Tabela za vikendice
CREATE TABLE vikendica (
    id INT AUTO_INCREMENT PRIMARY KEY,
    vlasnik VARCHAR(50),
    naziv VARCHAR(100),
    mesto VARCHAR(100),
    usluge TEXT,
    telefon VARCHAR(20),
    lat FLOAT,
    lon FLOAT,
    blokirana_do DATETIME,
    FOREIGN KEY (vlasnik) REFERENCES korisnik(korisnicko_ime)
);

-- Tabela za slike vikendica
CREATE TABLE slika (
    id INT AUTO_INCREMENT PRIMARY KEY,
    vikendica_id INT,
    putanja VARCHAR(255),
    FOREIGN KEY (vikendica_id) REFERENCES vikendica(id)
);

-- Tabela za cenovnik
CREATE TABLE cenovnik (
    id INT AUTO_INCREMENT PRIMARY KEY,
    vikendica_id INT,
    sezona VARCHAR(10) CHECK (sezona IN ('leto', 'zima')),
    cena DECIMAL(10,2),
    FOREIGN KEY (vikendica_id) REFERENCES vikendica(id)
);

-- Tabela za rezervacije
CREATE TABLE rezervacija (
    id INT AUTO_INCREMENT PRIMARY KEY,
    vikendica_id INT,
    turista VARCHAR(50),
    datum_od DATETIME,
    datum_do DATETIME,
    broj_odraslih INT,
    broj_dece INT,
    broj_kartice VARCHAR(20),
    opis TEXT,
    status VARCHAR(20) DEFAULT 'na_cekanju' CHECK (status IN ('na_cekanju', 'odobrena', 'odbijena', 'otkazana')),
    komentar_odbijanja TEXT,
    datum_rezervacije DATETIME DEFAULT NOW(),
    FOREIGN KEY (vikendica_id) REFERENCES vikendica(id),
    FOREIGN KEY (turista) REFERENCES korisnik(korisnicko_ime)
);

-- Tabela za komentare i ocene
CREATE TABLE arhiva (
    id INT AUTO_INCREMENT PRIMARY KEY,
    rezervacija_id INT,
    ocena INT CHECK (ocena BETWEEN 1 AND 5),
    tekst TEXT,
    datum DATETIME DEFAULT NOW()
);

-- Tabela za logove prijavljivanja
CREATE TABLE log_prijava (
    id INT AUTO_INCREMENT PRIMARY KEY,
    korisnicko_ime VARCHAR(50),
    datum_prijave DATETIME DEFAULT NOW(),
    uspeh TINYINT(1)
);

-- Tabela za istoriju blokiranja vikendica
CREATE TABLE istorija_blokiranja (
    id INT AUTO_INCREMENT PRIMARY KEY,
    vikendica_id INT,
    datum_blokiranja DATETIME DEFAULT NOW(),
    razlog TEXT,
    FOREIGN KEY (vikendica_id) REFERENCES vikendica(id)
);

-- INSERT korisnik
INSERT INTO korisnik (
    korisnicko_ime, lozinka_hash, ime, prezime, pol, adresa, telefon,
    email, profilna_slika_path, broj_kartice, uloga, aktivan
) VALUES
('admin', '$2a$12$8HDdCPmr.eQaWaD9O0d6DOASQUg0yuoMhBsqjXfIeSWC5OAoqZS7u', 'Admin', 'Adminović', 'M', 'Akademska 5', '+381603846758',
 'admin@example.com', 'admin.jpg', '5438465748394850', 'administrator', 1),

('anita', '$2a$12$InBcgRSQdGWGMPXYBUPRuOMakawBBXUzTGzoKnVq9dP2eR5bW237O', 'Anita', 'Anitović', 'Z', 'Zemunska 5', '+38163435769',
 'anita@primer.com', 'slika4.png', '4556273649586079', 'vlasnik', 1),

('maja', '$2a$12$WaaRSevqoTVNywAxoU9fKuIzKjW7yi8QL3gMTd8rcg5Vny4234v9q', 'Maja', 'Majić', 'Z', 'Maja 76', '+3816684504',
 'maja@primer.com', 'slika9.png', '4716574893049506', 'turista', 0),

('milan', '$2a$12$sm2x8.lQ8FdiSOg/OXmnbesueX9jeIce40VijfHgld1ErOH7QQvhG', 'Milan', 'Martić', 'M', 'Karađorđev trg 6', '+38161372938',
 'milan@primer.com', 'default.jpg', '3024836744885674', 'vlasnik', 1),

('nevena', '$2a$12$Ey4uEVdc9mmDdV7..6trl.bREhDQe9GZg43uU9.jcT/7mCj7Fz4cy', 'Nevena', 'Živanović', 'Z', 'Jurija Gagarina 15', '0624738495',
 'nevena@primer.com', 'slika6.jpeg', '5138495748337466', 'turista', 1),

('pera', '$2a$12$qIJEVZDAoQPseUDYdRg2bOc4QajLFuY0lJQ2KFIO7Rs8v9TL/VtTe', 'Pera', 'Perić', 'M', 'Omladinskih brigada 1', '064382948',
 'pera@primer.com', 'slika7.jpeg', '5243782940328405', 'vlasnik', 1),

('promi', '$2a$12$udxAzQAS1T0Hc.EV2ZlPge9sxQZy9io7OdzRgA7oOrJZxWG/7k.tW', 'Aleksandar', 'Promić', 'M', 'Aleksinačkih rudara 14', '+381603846758',
 'promi@primer.com', 'slika3.jpg', '5438465748394850', 'turista', 1),

('sonjuska', '$2a$12$qTVyATdWka8zp98PiGV2tO7c6jnQkh7S6XJVI1N4i97GLKHotZrfG', 'Sonja', 'Latinović', 'Z', 'Savski nasip 7', '0664839584',
 'sonja@primer.com', 'slika5.jpg', '301574839405864', 'vlasnik', 1),
 
('savo', '$2a$12$rUNubDda/vYb82xxNk/gf.ekKBmDEC61DUQZYbjSrl5dkqGb8don.', 'Savo', 'Savić', 'M', 'Njegoseva 1', '065843948',
 'savo@primer.com', 'default.jpg', '5343782940328405', 'turista', 1);
 
 -- INSERT vikendica
 INSERT INTO vikendica (vlasnik, naziv, mesto, usluge, telefon, lat, lon, blokirana_do) VALUES
('anita', 'Zemunski Raj', 'Zemun', 'wifi,parking,bazen', '+38163485769', 44.8399, 20.425, NULL),
('anita', 'Mirna Oaza', 'Zemun Polje', 'parking,roštilj,klima', '+38163485769', 44.873, 20.329, NULL),
('milan', 'Trg Vikendica', 'Beograd', 'wifi,klima,parking', '+381631372938', 44.816, 20.365, NULL),
('milan', 'Planinska Kuća', 'Avala', 'roštilj,kamin', '+381631372938', 44.689, 20.516, NULL),
('pera', 'Promina', 'Voždovac', 'wifi,roštilj,bazen', '+381603867458', 44.762, 20.477, NULL),
('pera', 'Aleksinačka Vikendica', 'Medaković', 'parking,klima', '+381603867458', 44.774, 20.499, NULL),
('sonjuska', 'Sonjina Oaza', 'Savski Nasip', 'klima,wifi,parking', '0664839384', 44.801, 20.435, NULL),
('sonjuska', 'Nasip Lux', 'Beograd', 'wifi,bazen,roštilj', '0664839384', 44.797, 20.408, NULL),
('sonjuska', 'Dunavski Mir', 'Beograd', 'wifi,klima,parking,roštilj', '0664839384', 44.823, 20.482, NULL);

-- INSERT slika
INSERT INTO slika (vikendica_id, putanja) VALUES
(1, 'vikendice/1/1.1.jpg'), (1, 'vikendice/1/1.2.jpg'), (1, 'vikendice/1/1.3.jpg'), (1, 'vikendice/1/1.4.jpg'), (1, 'vikendice/1/1.5.jpg'),
(1, 'vikendice/1/1.6.jpg'), (1, 'vikendice/1/1.7.jpg'), (1, 'vikendice/1/1.8.jpg'), (1, 'vikendice/1/1.9.jpg'), (1, 'vikendice/1/1.10.jpg'),
(1, 'vikendice/1/1.11.jpg'), (1, 'vikendice/1/1.12.jpg'), (1, 'vikendice/1/1.13.jpg'), (1, 'vikendice/1/1.14.jpg'), (1, 'vikendice/1/1.15.jpg'),
(1, 'vikendice/1/1.16.jpg'), (1, 'vikendice/1/1.17.jpg'),
(2, 'vikendice/2/2.1.jpg'), (2, 'vikendice/2/2.2.jpg'), (2, 'vikendice/2/2.3.jpg'), (2, 'vikendice/2/2.4.jpg'), (2, 'vikendice/2/2.5.jpg'),
(3, 'vikendice/3/3.1.jpg'), (3, 'vikendice/3/3.2.jpg'), (3, 'vikendice/3/3.3.jpg'), (3, 'vikendice/3/3.4.jpg'), (3, 'vikendice/3/3.5.jpg'),
(3, 'vikendice/3/3.6.jpg'), (3, 'vikendice/3/3.7.jpg'),
(4, 'vikendice/4/4.1.jpg'), (4, 'vikendice/4/4.2.jpg'), (4, 'vikendice/4/4.3.jpg'), (4, 'vikendice/4/4.4.jpg'), (4, 'vikendice/4/4.5.jpg'),
(4, 'vikendice/4/4.6.jpg'), (4, 'vikendice/4/4.7.jpg'), (4, 'vikendice/4/4.8.jpg'), (4, 'vikendice/4/4.9.jpg'), (4, 'vikendice/4/4.10.jpg'),
(4, 'vikendice/4/4.11.jpg'), (4, 'vikendice/4/4.12.jpg'), (4, 'vikendice/4/4.13.jpg'), (4, 'vikendice/4/4.14.jpg'), (4, 'vikendice/4/4.15.jpg'),
(5, 'vikendice/5/5.1.jpg'), (5, 'vikendice/5/5.2.jpg'), (5, 'vikendice/5/5.3.jpg'), (5, 'vikendice/5/5.4.jpg'), (5, 'vikendice/5/5.5.jpg'),
(5, 'vikendice/5/5.6.jpg'), (5, 'vikendice/5/5.7.jpg'), (5, 'vikendice/5/5.8.jpg'), (5, 'vikendice/5/5.9.jpg'), (5, 'vikendice/5/5.10.jpg'),
(6, 'vikendice/6/6.1.jpg'), (6, 'vikendice/6/6.2.jpg'), (6, 'vikendice/6/6.3.jpg'), (6, 'vikendice/6/6.4.jpg'), (6, 'vikendice/6/6.5.jpg'),
(6, 'vikendice/6/6.6.jpg'), (6, 'vikendice/6/6.7.jpg'),
(7, 'vikendice/7/7.1.jpg'), (7, 'vikendice/7/7.2.jpg'), (7, 'vikendice/7/7.3.jpg'), (7, 'vikendice/7/7.4.jpg'), (7, 'vikendice/7/7.5.jpg'),
(8, 'vikendice/8/8.1.jpg'), (8, 'vikendice/8/8.2.jpg'), (8, 'vikendice/8/8.3.jpg'), (8, 'vikendice/8/8.4.jpg'), (8, 'vikendice/8/8.5.jpg'),
(9, 'vikendice/9/9.1.jpg'), (9, 'vikendice/9/9.2.jpg'), (9, 'vikendice/9/9.3.jpg'), (9, 'vikendice/9/9.4.jpg'), (9, 'vikendice/9/9.5.jpg');


-- INSERT cenovnik
-- Zemunski Raj
INSERT INTO cenovnik (vikendica_id, sezona, cena) VALUES (1, 'leto', 1500);
INSERT INTO cenovnik (vikendica_id, sezona, cena) VALUES (1, 'zima', 1700);
-- Mirna Oaza
INSERT INTO cenovnik (vikendica_id, sezona, cena) VALUES (2, 'leto', 1200);
INSERT INTO cenovnik (vikendica_id, sezona, cena) VALUES (2, 'zima', 1000);

-- Trg Vikendica
INSERT INTO cenovnik (vikendica_id, sezona, cena) VALUES (3, 'leto', 2000);
INSERT INTO cenovnik (vikendica_id, sezona, cena) VALUES (3, 'zima', 1800);
-- Planinska Kuća
INSERT INTO cenovnik (vikendica_id, sezona, cena) VALUES (4, 'leto', 1500);
INSERT INTO cenovnik (vikendica_id, sezona, cena) VALUES (4, 'zima', 1800);
-- Promina
INSERT INTO cenovnik (vikendica_id, sezona, cena) VALUES (5, 'leto', 1800);
INSERT INTO cenovnik (vikendica_id, sezona, cena) VALUES (5, 'zima', 1000);
-- Aleksinačka Vikendica
INSERT INTO cenovnik (vikendica_id, sezona, cena) VALUES (6, 'leto', 1900);
INSERT INTO cenovnik (vikendica_id, sezona, cena) VALUES (6, 'zima', 1200);
-- Sonjina Oaza
INSERT INTO cenovnik (vikendica_id, sezona, cena) VALUES (7, 'leto', 1300);
INSERT INTO cenovnik (vikendica_id, sezona, cena) VALUES (7, 'zima', 1600);
-- Nasip Lux
INSERT INTO cenovnik (vikendica_id, sezona, cena) VALUES (8, 'leto', 1200);
INSERT INTO cenovnik (vikendica_id, sezona, cena) VALUES (8, 'zima', 1100);
-- Dunavski mir
INSERT INTO cenovnik (vikendica_id, sezona, cena) VALUES (9, 'leto', 900);
INSERT INTO cenovnik (vikendica_id, sezona, cena) VALUES (9, 'zima', 800);

/* === 200 rezervacija: 2025-01-01 .. 2027-12-31 (MySQL 5.7 kompatibilno) === */
INSERT INTO rezervacija (
  vikendica_id, turista, datum_od, datum_do,
  broj_odraslih, broj_dece, broj_kartice, opis,
  status, komentar_odbijanja, datum_rezervacije
)
SELECT
  /* vikendica 1..9 nasumično deterministički po n */
  1 + FLOOR(RAND(n*7919) * 9) AS vikendica_id,

  /* nasumični turista iz skupa */
  CASE FLOOR(RAND(n*1237) * 4)
    WHEN 0 THEN 'nevena'
    WHEN 1 THEN 'promi'
    WHEN 2 THEN 'maja'
    ELSE 'savo'
  END AS turista,

  /* datum_od: slučajan dan u opsegu (14:00 check-in) */
  DATE(datum_od_raw) + INTERVAL 14 HOUR AS datum_od,

  /* datum_do: trajanje 2–7 dana (10:00 check-out) */
  DATE(datum_od_raw) + INTERVAL trajanje_dana DAY + INTERVAL 10 HOUR AS datum_do,

  /* odrasli 1–4, deca 0–2 */
  1 + FLOOR(RAND(n*3331) * 4) AS broj_odraslih,
  FLOOR(RAND(n*4447) * 3)     AS broj_dece,

  /* broj kartice mapiran po turisti (da prođu FK + logika) */
  CASE FLOOR(RAND(n*1237) * 4)
    WHEN 0 THEN '5138495748337466'  /* nevena */
    WHEN 1 THEN '5438465748394850'  /* promi  */
    WHEN 2 THEN '4716574893049506'  /* maja   */
    ELSE '5343782940328405'         /* savo   */
  END AS broj_kartice,

  CONCAT('Auto-test rezervacija #', LPAD(n,3,'0')) AS opis,

  /* status nasumično iz dozvoljenih */
  CASE FLOOR(RAND(n*9871) * 4)
    WHEN 0 THEN 'na_cekanju'
    WHEN 1 THEN 'odobrena'
    WHEN 2 THEN 'odbijena'
    ELSE 'otkazana'
  END AS status,

  /* komentar za odbijene/otkazane, ostalo prazno */
  CASE
    WHEN FLOOR(RAND(n*9871) * 4) = 2 THEN 'Odbijeno: termin nije dostupan / uslovi neispunjeni'
    WHEN FLOOR(RAND(n*9871) * 4) = 3 THEN 'Otkazano od strane gosta'
    ELSE ''
  END AS komentar_odbijanja,

  /* datum rezervacije 3–30 dana pre dolaska (09:00) */
  (DATE(datum_od_raw) - INTERVAL lead_days DAY) + INTERVAL 9 HOUR AS datum_rezervacije

FROM (
  /* generator n = 1..200 preko digits tabela + varijable (MySQL 5.7 friendly) */
  SELECT
    (@n:=@n+1) AS n,

    /* nasumičan raw datum u [start, end-10d] */
    FROM_UNIXTIME(
      UNIX_TIMESTAMP('2025-01-01 00:00:00') +
      FLOOR(
        RAND((@n+1)*1777) * (
          UNIX_TIMESTAMP('2027-12-31 23:59:59' - INTERVAL 10 DAY) -
          UNIX_TIMESTAMP('2025-01-01 00:00:00')
        )
      )
    ) AS datum_od_raw,

    /* trajanje 2–7 dana */
    2 + FLOOR(RAND((@n+1)*2851) * 6) AS trajanje_dana,

    /* lead days 3–30 */
    3 + FLOOR(RAND((@n+1)*6551) * 28) AS lead_days

  FROM
    /* digits 0..9 */
    (SELECT 0 d UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
     UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) d0
  CROSS JOIN
    (SELECT 0 d UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
     UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) d1
  CROSS JOIN
    (SELECT 0 d UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
     UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) d2
  /* 10*10*10 = 1000 redova; uzmemo prvih 200 */
  CROSS JOIN (SELECT @n:=0) vars
  LIMIT 200
) g;
