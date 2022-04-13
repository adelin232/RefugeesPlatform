USE DB_REFUGEES

CREATE PROCEDURE FIND_USER_BY_EMAIL(IN email_in VARCHAR(50))
BEGIN 
    SELECT * FROM user WHERE email = email_in;
END;

INSERT INTO room (id, num, floor, size, is_avail, link, address, lat, lon)
VALUES (1, 26, 'First Floor', 2, 1, 'https://studioinsign.ro/wp-content/uploads/2016/08/Proiecte-hoteluri-design-interior-camera-hotel-stil-loft-industrial-newyorkez-1.png', 'Str. Pietroasele, Nr. 18', 46.0125, 25.0567);

INSERT INTO room (id, num, floor, size, is_avail, link, address, lat, lon)
VALUES (2, 13, 'Second Floor', 3, 1, 'https://idecorate.ro/wp-content/uploads/2017/04/design-interior-dormitor-5.jpg', 'Str. Bazalt, Nr. 29', 46.0189, 25.0521);
