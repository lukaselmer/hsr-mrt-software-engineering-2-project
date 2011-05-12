/*
 * Autoren: Delia Treichler, Christina Heidt, Lukas Elmer
 */ 


/*
 * Gibt die Client-ID's aus, bei denen der Aussendienstmitarbeiter mit der User-ID = 1 gearbeitet hat
 */
SELECT DISTINCT client_id AS "Client ID" 
FROM worktime
WHERE generaluser_id = 1;


/*
 * Gibt Client-ID, Vorname, Nachname und Wohnort der Clients aus, bei denen Arbeiten vom 
 * Aussendienstmitarbeiter mit der User-ID = 2 ausgeführt wurden
 */ 
SELECT distinct worktime.client_id AS "Client ID", client.first_name AS "Vorname", client.last_name AS "Nachname", address.place AS "Wohnort" 
FROM worktime, client, address
WHERE worktime.generaluser_id = 2
AND address.client_id = client.id
AND client.id = worktime.client_id;


/*
 * Gibt den Auftrag mit Startzeit und Datum aus, welcher zeitlich am fruehesten begonnen hat
 * (Gibt es mehrere mit der gleichen fruehen Zeit, so werden alle diese ausgegeben)
 */
SELECT generaluser_id AS "User ID", time_start AS "Startzeit", date_worked AS "Datum" 
FROM worktime
WHERE time_start = (
	SELECT MIN (time_start)
	FROM worktime
);


/*
 * Gibt Anzahl Auftraege für jeden User aus, mit User-ID und Userlogin
 */
SELECT g.id AS "User ID", g.login AS "Userlogin", COUNT (*) AS "Anz. Auftraege" 
FROM worktime w
INNER JOIN generaluser g ON w.generaluser_id = g.id
GROUP BY g.id, g.login;


/*
 * Gibt die Clients aus mit ID, Vorname und Nachname, für die keine Arbeiten ausgefuehrt wurden
 */
SELECT id AS "Client-ID", first_name AS "Vorname", last_name AS "Nachname" 
FROM client
WHERE id NOT IN (
	SELECT client_id
	FROM worktime
);

