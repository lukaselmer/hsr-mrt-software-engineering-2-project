create table generaluser(
ID INTEGER PRIMARY KEY,
login VARCHAR(50) NOT NULL,
crypted_password CHAR(16) NOT NULL, 
last_login DATE,
type INTEGER NOT NULL);

create table worktime(
ID INTEGER PRIMARY KEY,
date_worked DATE NOT NULL,
time_start TIME NOT NULL,
time_stop TIME);

create table client(
ID INTEGER PRIMARY KEY,
first_name VARCHAR(100) NOT NULL,
last_name VARCHAR(100) NOT NULL,
phone_number VARCHAR(15) NOT NULL);

create table address(
address_line_1 VARCHAR(100) NOT NULL,
address_line_2 VARCHAR(100) NOT NULL,
address_line_3 VARCHAR(100),
latitude DOUBLE,
longitude DOUBLE,
place VARCHAR(100) NOT NULL,
zip INTEGER NOT NULL);
