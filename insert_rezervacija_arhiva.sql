INSERT INTO rezervacija (
    vikendica_id, turista, datum_od, datum_do,
    broj_odraslih, broj_dece, broj_kartice, opis,
    status, komentar_odbijanja, datum_rezervacije
) VALUES 
(17, 'promi', '2024-08-01', '2024-08-03', 2, 1, '5438465748394850', 'Letnji vikend', 'odobrena', '', '2024-07-29'),
(17, 'promi', '2024-08-10', '2024-08-12', 1, 0, '5438465748394850', 'Samostalno putovanje', 'odobrena', '', '2024-07-29'),
(18, 'promi', '2024-08-15', '2024-08-17', 3, 2, '5438465748394850', 'Porodični odmor', 'odobrena', '', '2024-07-29'),
(13, 'promi', '2025-09-01', '2025-09-05', 2, 0, '5438465748394850', 'Radna nedelja', 'odobrena', '', '2025-07-29'),
(12, 'promi', '2025-09-10', '2025-09-12', 2, 0, '5438465748394850', 'Brzi vikend beg', 'odobrena', '', '2025-07-29');

INSERT INTO rezervacija (
    vikendica_id, turista, datum_od, datum_do,
    broj_odraslih, broj_dece, broj_kartice, opis,
    status, komentar_odbijanja, datum_rezervacije
) VALUES 
(11, 'nevena', '2024-08-05', '2024-08-08', 2, 0, '1234567890123456', 'Odmor u Zemunu', 'odobrena', '', '2024-07-29'),
(12, 'nevena', '2024-08-20', '2024-08-22', 1, 1, '1234567890123456', 'Porodični vikend', 'odobrena', '', '2024-07-29'),
(13, 'promi', '2024-09-01', '2024-09-03', 2, 0, '9876543210987654', 'Odmor u Beogradu', 'odobrena', '', '2024-07-29'),
(14, 'promi', '2024-09-10', '2024-09-13', 2, 1, '9876543210987654', 'Planinski vikend', 'odobrena', '', '2024-07-29'),
(15, 'nevena', '2025-10-15', '2025-10-18', 2, 0, '4444333322221111', 'Opuštanje u prirodi', 'odobrena', '', '2025-07-29'),
(16, 'nevena', '2025-10-05', '2025-10-07', 1, 0, '4444333322221111', 'Miran vikend', 'odobrena', '', '2025-07-29');

INSERT INTO arhiva (rezervacija_id, ocena, tekst, datum) VALUES
((SELECT id FROM rezervacija WHERE turista = 'promi' AND datum_do = '2024-08-03' LIMIT 1), 5, 'Savršen vikend! Sve preporuke.', '2024-08-04'),
((SELECT id FROM rezervacija WHERE turista = 'promi' AND datum_do = '2024-08-12' LIMIT 1), 4, 'Lepo iskustvo, ali komarci su smetali.', '2024-08-13'),
((SELECT id FROM rezervacija WHERE turista = 'promi' AND datum_do = '2024-08-17' LIMIT 1), 5, 'Sjajno za porodicu!', '2024-08-18');

INSERT INTO arhiva (rezervacija_id, ocena, tekst, datum) VALUES
((SELECT id FROM rezervacija WHERE turista = 'nevena' AND vikendica_id = 11 AND datum_do = '2024-08-08' LIMIT 1), 4, 'Prijatno iskustvo u Zemunu.', '2024-08-09'),
((SELECT id FROM rezervacija WHERE turista = 'nevena' AND vikendica_id = 12 AND datum_do = '2024-08-22' LIMIT 1), 5, 'Odlično mesto za porodični vikend!', '2024-08-23'),
((SELECT id FROM rezervacija WHERE turista = 'promi' AND vikendica_id = 13 AND datum_do = '2024-09-03' LIMIT 1), 4, 'Sve ok, ali komšije su bile bučne.', '2024-09-04'),
((SELECT id FROM rezervacija WHERE turista = 'promi' AND vikendica_id = 14 AND datum_do = '2024-09-13' LIMIT 1), 5, 'Predivan pogled i čist vazduh!', '2024-09-14');

INSERT INTO rezervacija (
    vikendica_id, turista, datum_od, datum_do,
    broj_odraslih, broj_dece, broj_kartice, opis,
    status, komentar_odbijanja, datum_rezervacije
) VALUES 
(11, 'nevena', '2024-01-05', '2024-01-08', 2, 0, '1234567890123456', 'Relax', 'odobrena', '', '2023-11-29'),
(11, 'promi', '2024-02-05', '2024-02-08', 2, 0, '1234567890123456', 'Pauzica od ucenja', 'odobrena', '', '2023-12-29'),
(11, 'nevena', '2024-03-05', '2024-03-08', 2, 1, '1234567890123456', 'Relax', 'odobrena', '', '2024-1-29'),
(11, 'nevena', '2024-05-05', '2024-05-08', 1, 0, '1234567890123456', 'Opet ja kod vas.', 'odobrena', '', '2023-08-29'),
(11, 'promi', '2024-04-05', '2024-04-08', 4, 0, '1234567890123456', 'Idemo na put.', 'odobrena', '', '2023-06-29');

INSERT INTO arhiva (rezervacija_id, ocena, tekst, datum) VALUES
((SELECT id FROM rezervacija WHERE turista = 'nevena' AND datum_do = '2024-01-08' LIMIT 1), 4, 'Vrlo lep ambijent, sve je bilo u redu.', '2024-01-09'),
((SELECT id FROM rezervacija WHERE turista = 'promi' AND datum_do = '2024-02-08' LIMIT 1), 5, 'Pauza od učenja uspela! Sve preporuke.', '2024-02-09'),
((SELECT id FROM rezervacija WHERE turista = 'nevena' AND datum_do = '2024-03-08' LIMIT 1), 3, 'Bilo je lepo, ali je moglo biti čistije.', '2024-03-09'),
((SELECT id FROM rezervacija WHERE turista = 'promi' AND datum_do = '2024-04-08' LIMIT 1), 5, 'Idemo na put – i bilo je savršeno!', '2024-04-09'),
((SELECT id FROM rezervacija WHERE turista = 'nevena' AND datum_do = '2024-05-08' LIMIT 1), 5, 'Opet ja kod vas – i opet sjajno!', '2024-05-09');
