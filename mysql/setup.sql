USE db_refugees

DELIMITER //

CREATE PROCEDURE FIND_USER_BY_EMAIL(IN email_in VARCHAR(50))
BEGIN 
    SELECT * FROM user WHERE email = email_in;
END
//

DELIMITER ;

CREATE TABLE room (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	address VARCHAR(255) NOT NULL,
	floor VARCHAR(255) NOT NULL,
	is_avail BIT(1) NOT NULL,
	lat DOUBLE NOT NULL,
	link VARCHAR(255) NOT NULL,
	lon DOUBLE NOT NULL,
	num BIGINT NOT NULL,
	size BIGINT NOt NULL
);

INSERT INTO room (num, floor, size, is_avail, link, address, lat, lon)
VALUES (26, 'First Floor', 2, 1, 'https://studioinsign.ro/wp-content/uploads/2016/08/Proiecte-hoteluri-design-interior-camera-hotel-stil-loft-industrial-newyorkez-1.png', 'Str. Pietroasele, Nr. 18', 46.0125, 25.0567);

INSERT INTO room (num, floor, size, is_avail, link, address, lat, lon)
VALUES (13, 'Second Floor', 3, 1, 'https://idecorate.ro/wp-content/uploads/2017/04/design-interior-dormitor-5.jpg', 'Str. Bazalt, Nr. 29', 46.0189, 25.0521);
