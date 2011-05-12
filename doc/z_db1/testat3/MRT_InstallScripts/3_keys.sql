/*
 * Autoren: Delia Treichler, Christina Heidt, Lukas Elmer
 */ 

ALTER TABLE only worktime
ADD CONSTRAINT fk_worktime_generaluser
	FOREIGN KEY (generaluser_id) REFERENCES generaluser(id)
ON DELETE CASCADE;

ALTER TABLE only worktime
ADD CONSTRAINT fk_worktime_client
	FOREIGN KEY (client_id) REFERENCES client(id)
ON DELETE CASCADE;

ALTER TABLE only address
ADD CONSTRAINT fk_address_client
	FOREIGN KEY (client_id) REFERENCES client(id)
ON DELETE CASCADE;
