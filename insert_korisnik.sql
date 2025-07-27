INSERT INTO korisnik (
    korisnicko_ime, lozinka_hash, ime, prezime, pol, adresa, telefon, email,
    profilna_slika_path, broj_kartice, uloga, aktivan
) VALUES
('admin1', 'hashed_pwd_1', 'Ana', 'Adminovic', 'Z', 'Beograd 1', '0601234567', 'admin1@example.com',
 '/slike/admin1.png', '4539551111111111', 'administrator', 1),

('vlasnik1', 'hashed_pwd_2', 'Vlada', 'Vikendic', 'M', 'Zlatibor 45', '0612233445', 'vlasnik1@example.com',
 '/slike/vlasnik1.png', '5311222233334444', 'vlasnik', 1),

('vlasnik2', 'hashed_pwd_3', 'Mira', 'Planinkovic', 'Z', 'Tara bb', '0639988776', 'vlasnik2@example.com',
 NULL, '300123456789012', 'vlasnik', 0),

('turista1', 'hashed_pwd_4', 'Jovan', 'Turistovic', 'M', 'Novi Sad 88', '0651234567', 'turista1@example.com',
 '/slike/turista1.png', '4556333344445555', 'turista', 1),

('turista2', 'hashed_pwd_5', 'Ivana', 'Putnik', 'Z', 'Niš 99', '0645554443', 'turista2@example.com',
 NULL, '4916111111111111', 'turista', 0);
