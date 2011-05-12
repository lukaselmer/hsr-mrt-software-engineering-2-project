/*
 * Autoren: Delia Treichler, Christina Heidt, Lukas Elmer
 */

-- TABLE  GENERALUSER

INSERT INTO generaluser (id, login, crypted_password, type) VALUES (1, 'aarglos', md5('abc'), 1);
INSERT INTO generaluser (id, login, crypted_password, type) VALUES (2, 'ttuechtig', md5('123'), 1);

-- TABLE  CLIENT

INSERT INTO client (id, first_name, last_name, phone_number) VALUES (1, 'delia', 'treichler', '99');
INSERT INTO client (id, first_name, last_name, phone_number) VALUES (2, 'christina', 'heidt', '88');
INSERT INTO client (id, first_name, last_name, phone_number) VALUES (3, 'lukas', 'elmer', '77');
INSERT INTO client (id, first_name, last_name, phone_number) VALUES (4, 'diego', 'steiner', '66');
INSERT INTO client (id, first_name, last_name, phone_number) VALUES (5, 'remo', 'waltenspuel', '55');
INSERT INTO client (id, first_name, last_name, phone_number) VALUES (6, 'marcel', 'steiner', '44');
INSERT INTO client (id, first_name, last_name, phone_number) VALUES (7, 'simone', 'schneider', '33');
INSERT INTO client (id, first_name, last_name, phone_number) VALUES (8, 'franziska', 'schaepper', '22');
 
-- TABLE  ADDRESS

INSERT INTO address (address_line_1, address_line_2, place, zip, client_id) VALUES ('stinksoeckli AG', 'Postfach 99', 'Wetzikon', 8620, 1);
INSERT INTO address (address_line_1, address_line_2, place, zip, client_id) VALUES ('Schlafmuetzli GmbH', 'Postfach 88', 'Zollikerberg', 8125, 2);
INSERT INTO address (address_line_1, address_line_2, place, zip, client_id) VALUES ('Fun AG', 'Postfach 77', 'Zuerich', 8046, 3);
INSERT INTO address (address_line_1, address_line_2, place, zip, client_id) VALUES ('Sterndli GmbH', 'Postfach 66', 'Zuerich', 8046, 4);
INSERT INTO address (address_line_1, address_line_2, place, zip, client_id) VALUES ('Uster Technologies AG', 'Postfach 55', 'Gossau', 8625, 5);
INSERT INTO address (address_line_1, address_line_2, place, zip, client_id) VALUES ('CattaTalk GmbH', 'Postfach 44', 'Ebnat-Kappel', 9642, 6);
INSERT INTO address (address_line_1, address_line_2, place, zip, client_id) VALUES ('Mlle Schneider', 'Postfach 33', 'Rueti', 8630, 7);
INSERT INTO address (address_line_1, address_line_2, place, zip, client_id) VALUES ('allesundnuet AG', 'Postfach 22', 'Nesslau', 9650 , 8);
 
-- TABLE  WORKTIME

INSERT INTO worktime (id, generaluser_id, client_id, date_worked, time_start, time_stop) VALUES (1, 1, 1, '2011-04-01', '08:30', '10:01');
INSERT INTO worktime (id, generaluser_id, client_id, date_worked, time_start, time_stop) VALUES (2, 1, 5, '2011-04-01', '10:25', '12:01');
INSERT INTO worktime (id, generaluser_id, client_id, date_worked, time_start, time_stop) VALUES (3, 1, 2, '2011-04-01', '13:14', '15:14');
INSERT INTO worktime (id, generaluser_id, client_id, date_worked, time_start, time_stop) VALUES (4, 1, 6, '2011-04-04', '08:00', '08:33');
INSERT INTO worktime (id, generaluser_id, client_id, date_worked, time_start, time_stop) VALUES (5, 1, 3, '2011-04-04', '14:55', '16:28');
INSERT INTO worktime (id, generaluser_id, client_id, date_worked, time_start, time_stop) VALUES (6, 1, 3, '2011-04-05', '08:05', '11:36');
INSERT INTO worktime (id, generaluser_id, client_id, date_worked, time_start, time_stop) VALUES (7, 1, 2, '2011-04-05', '12:44', '15:00');
INSERT INTO worktime (id, generaluser_id, client_id, date_worked, time_start, time_stop) VALUES (8, 1, 7, '2011-04-05', '15:30', '17:21');
INSERT INTO worktime (id, generaluser_id, client_id, date_worked, time_start, time_stop) VALUES (9, 2, 1, '2011-04-04', '09:00', '11:00');
INSERT INTO worktime (id, generaluser_id, client_id, date_worked, time_start, time_stop) VALUES (10, 2, 2, '2011-04-04', '13:00', '17:00');
INSERT INTO worktime (id, generaluser_id, client_id, date_worked, time_start, time_stop) VALUES (11, 2, 3, '2011-04-05', '08:00', '10:00');
INSERT INTO worktime (id, generaluser_id, client_id, date_worked, time_start, time_stop) VALUES (12, 2, 8, '2011-04-05', '11:00', '12:00');
INSERT INTO worktime (id, generaluser_id, client_id, date_worked, time_start, time_stop) VALUES (13, 2, 1, '2011-04-06', '09:00', '11:30');