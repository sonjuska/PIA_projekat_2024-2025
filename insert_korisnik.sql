INSERT INTO korisnik (
    korisnicko_ime, lozinka_hash, ime, prezime, pol, adresa, telefon,
    email, profilna_slika_path, broj_kartice, uloga, aktivan
) VALUES
('admin', '$2a$12$8HDdCPmr.eQaWaD9O0d6DOASQUg0yuoMhBsqjXfIeSWC5OAoqZS7u', 'Admin', 'Adminović', 'M', 'Akademska 5', '+381603846758',
 'admin@example.com', 'admin.jpg', '5438465748394850', 'administrator', 1),

('anita', '$2a$12$InBcgRSQdGWGMPXYBUPRuOMakawBBXUzTGzoKnVq9dP2eR5bW237O', 'Anita', 'Anitović', 'Z', 'Zemunska 5', '+38163435769',
 'anita@primer.com', 'slika4.png', '4556273649586079', 'vlasnik', 1),

('maja', '$2a$12$WK5JHHYQgGycA3D7MJLK1e8sELtVvsZ9xFVcwOvFrsaZd2K2XCzKq', 'Maja', 'Majić', 'Z', 'Maja 76', '+3816684504',
 'maja@primer.com', 'slika9.png', '4716574893049506', 'turista', 0),

('milan', '$2a$12$sm2x8.lQ8FdiSOg/OXmnbesueX9jeIce40VijfHgld1ErOH7QQvhG', 'Milan', 'Martić', 'M', 'Karađorđev trg 6', '+38161372938',
 'milan@primer.com', 'default.jpg', '3024836744885674', 'vlasnik', 1),

('nevena', '$2a$12$Ey4uEVdc9mmDdV7..6trl.bREhDQe9GZg43uU9.jcT/7mCj7Fz4cy', 'Nevena', 'Živanović', 'Z', 'Jurija Gagarina 15', '0624738495',
 'nevena@primer.com', 'slika6.jpeg', '5138495748337466', 'turista', 1),

('pera', '$2a$12$d17bbzRQDEZW088fO.C.9u/bsG8Zs3ukJAFbYhLQbbX1zN7rs7N8y', 'Pera', 'Perić', 'M', 'Omladinskih brigada 1', '064382948',
 'pera@primer.com', 'slika7.jpeg', '5243782940328405', 'vlasnik', 1),

('promi', '$2a$12$udxAzQAS1T0Hc.EV2ZlPge9sxQZy9io7OdzRgA7oOrJZxWG/7k.tW', 'Aleksandar', 'Promić', 'M', 'Aleksinačkih rudara 14', '+381603846758',
 'promi@primer.com', 'slika3.jpg', '5438465748394850', 'turista', 1),

('sonjuska', '$2a$12$qTVyATdWka8zp98PiGV2tO7c6jnQkh7S6XJVI1N4i97GLKHotZrfG', 'Sonja', 'Latinović', 'Z', 'Savski nasip 7', '0664839584',
 'sonja@primer.com', 'slika5.jpg', '301574839405864', 'vlasnik', 1);

