/*
 * Autoren: Delia Treichler, Christina Heidt, Lukas Elmer
 */

CREATE TABLE generaluser(
id INTEGER PRIMARY KEY,
login VARCHAR(50) NOT NULL,
crypted_password CHAR(32) NOT NULL, 
last_login DATE,
type INTEGER NOT NULL
);

CREATE TABLE worktime(
id INTEGER PRIMARY KEY,
generaluser_id INTEGER,
client_id INTEGER,
date_worked DATE NOT NULL,
time_start TIME NOT NULL,
time_stop TIME
);

CREATE TABLE client(
id INTEGER PRIMARY KEY,
first_name VARCHAR(100) NOT NULL,
last_name VARCHAR(100) NOT NULL,
phone_number VARCHAR(15) NOT NULL
);

CREATE TABLE address(
address_line_1 VARCHAR(100) NOT NULL,
address_line_2 VARCHAR(100) NOT NULL,
address_line_3 VARCHAR(100),
latitude DOUBLE PRECISION,
longitude DOUBLE PRECISION,
place VARCHAR(100) NOT NULL,
zip INTEGER NOT NULL,
client_id INTEGER
);