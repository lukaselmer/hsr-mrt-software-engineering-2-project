/*
 * Autoren: Delia Treichler, Christina Heidt, Lukas Elmer
 */ 

/*
 * Savepoint erstellen
 */
SELECT * FROM address;
BEGIN;
SAVEPOINT mysavepoint;




/*
 * Update machen
 */
UPDATE address SET zip = 5555;
SELECT * FROM address;


/*
 * Rollback ausführen
 */
ROLLBACK TO mysavepoint;
SELECT * FROM address;


/*
 * Update erneut machen, dieses Mal richtig
 */
UPDATE address SET zip = 5555 WHERE client_id = 4;
COMMIT;
SELECT * FROM address;

