USE DB_REFUGEES

DELIMITER #

INSERT INTO user ("email@gmail.com");

CREATE PROCEDURE FIND_USER_BY_EMAIL(IN email_in VARCHAR(50))
BEGIN 
    SELECT * FROM user WHERE email = email_in;
END#

DELIMITER ;
