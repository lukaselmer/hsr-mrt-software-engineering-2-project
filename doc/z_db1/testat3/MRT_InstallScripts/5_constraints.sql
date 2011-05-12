/*
 * Autoren: Delia Treichler, Christina Heidt, Lukas Elmer
 */ 


/*
 * Vorbereitung
 */
CREATE TYPE status_type AS ENUM ('inactive', 'normal', 'new');
ALTER TABLE client ADD COLUMN status status_type;
UPDATE client SET status = 'normal';

UPDATE address SET zip = 8000 WHERE client_id = 1 OR client_id = 3 OR client_id = 7;

ALTER TABLE client ADD COLUMN phone_number_dup varchar;
UPDATE client SET phone_number_dup = phone_number;
UPDATE client SET phone_number = '+41 98 76 ' || phone_number_dup;
ALTER TABLE client DROP COLUMN phone_number_dup;



SELECT * FROM generaluser;


/*
 * Enum
 */
CREATE TYPE working_type AS ENUM ('secretary', 'field_worker');
ALTER TABLE generaluser ADD COLUMN type_new working_type;
UPDATE generaluser SET type_new = 'field_worker' WHERE type = 1;
ALTER TABLE generaluser DROP COLUMN type;
ALTER TABLE generaluser RENAME COLUMN type_new TO type;

INSERT INTO generaluser VALUES (3, 'kkunz', md5('mrt'), NULL, 'secretary');
/*
 * Enum: dieser Insert schlägt fehl, da falscher Enum-Type
 */
INSERT INTO generaluser VALUES (4, 'ffroh', md5('mrt'), NULL, 'worker');

SELECT * FROM generaluser;


/*
 * Constraints
 */
ALTER TABLE client ADD CONSTRAINT phone_number_cnstr CHECK ( phone_number LIKE '+41 %' );
INSERT INTO client VALUES (9, 'gabi', 'gut', '+41 98 76 11', 'new');
/*
 * Constraints: dieser Insert schlägt fehl, da falsche Tel-Nr
 */
INSERT INTO client VALUES (10, 'franz', 'froh', '98 76 00', 'new');

ALTER TABLE generaluser ADD CONSTRAINT login_lenght  CHECK (LENGTH(login) < 10);
INSERT INTO generaluser VALUES (3, 'hherrmann', md5('mrt'), null, 'secretary');
/*
 * Constraints: dieser Insert schlägt fehl, da zu langer Name
 */
INSERT INTO generaluser VALUES (4, 'ffroehlich', md5('mrt'), null, 'secretary');




