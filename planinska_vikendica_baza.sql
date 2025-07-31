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
    datum DATETIME DEFAULT NOW(),
    FOREIGN KEY (rezervacija_id) REFERENCES rezervacija(id)
);

-- Tabela za logove prijavljivanja
CREATE TABLE log_prijava (
    id INT AUTO_INCREMENT PRIMARY KEY,
    korisnicko_ime VARCHAR(50),
    datum_prijave DATETIME DEFAULT NOW(),
    uspeh TINYINT(1),
    FOREIGN KEY (korisnicko_ime) REFERENCES korisnik(korisnicko_ime)
);

-- Tabela za istoriju blokiranja vikendica
CREATE TABLE istorija_blokiranja (
    id INT AUTO_INCREMENT PRIMARY KEY,
    vikendica_id INT,
    datum_blokiranja DATETIME DEFAULT NOW(),
    razlog TEXT,
    FOREIGN KEY (vikendica_id) REFERENCES vikendica(id)
);
