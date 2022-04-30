USE db_refugees

DELIMITER //

CREATE PROCEDURE FIND_USER_BY_EMAIL(IN email_in VARCHAR(50))
BEGIN 
    SELECT * FROM users WHERE email = email_in;
END
//

CREATE PROCEDURE FIND_RENTAL_BY_USERID(IN user_id_in BIGINT)
BEGIN 
    SELECT * FROM rentals WHERE user_id = user_id_in;
END
//

DELIMITER ;

CREATE TABLE rooms (
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

INSERT INTO rooms (num, floor, size, is_avail, link, address, lat, lon)
VALUES (26, 'First Floor', 2, 1, 'https://studioinsign.ro/wp-content/uploads/2016/08/Proiecte-hoteluri-design-interior-camera-hotel-stil-loft-industrial-newyorkez-1.png', 'Str. Pietroasele, Nr. 18', 46.0125, 25.0567);

CREATE TABLE rentals (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	user_id BIGINT NOT NULL,
	room_id BIGINT NOT NULL,
	start_date VARCHAR(255) NOT NULL,
	end_date VARCHAR(255) NOT NULL
);
