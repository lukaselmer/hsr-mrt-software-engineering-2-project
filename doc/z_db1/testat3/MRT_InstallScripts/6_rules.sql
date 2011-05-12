/*
 * Autoren: Delia Treichler, Christina Heidt, Lukas Elmer
 */ 

/*
 * View
 */
CREATE VIEW client_address_view AS
SELECT c.first_name, c.last_name, a.address_line_1, a.address_line_2, a.zip, a.place, c.phone_number, c.status FROM client c, address a
WHERE c.id = a.client_id
ORDER BY c.last_name;
SELECT * FROM client_address_view;


/*
 * Rules: der Status soll sich updaten, wenn ein ZIP-Code geändert wurde
 */
CREATE RULE rule_zip AS ON UPDATE
TO client_address_view DO INSTEAD UPDATE address SET zip = NEW.zip WHERE address_line_1 = NEW.address_line_1;
CREATE RULE rule_status AS ON UPDATE
TO client_address_view DO INSTEAD UPDATE client SET status = 'new' WHERE last_name = NEW.last_name;


/*
 * Einige ZIP Codes ändern
 */
-- 
UPDATE client_address_view SET zip = 8064 WHERE last_name LIKE 'elmer%';
UPDATE client_address_view SET zip = 8630 WHERE last_name LIKE 'schneider%';
UPDATE client_address_view SET zip = 8620 WHERE last_name LIKE 'treichler%';
SELECT * FROM client_address_view;