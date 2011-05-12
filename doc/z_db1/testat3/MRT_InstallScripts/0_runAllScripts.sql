/*
 * Autoren: Delia Treichler, Christina Heidt, Lukas Elmer
 */ 

\set user mrt_user 
\set password '\'mrtproj\''
\set database mrt_proj

\echo -------------------------------------
\echo Passwort fuer User :user = :password
\echo -------------------------------------

DROP DATABASE :database;
DROP USER :user ;

CREATE USER :user WITH PASSWORD :password;
CREATE DATABASE :database WITH OWNER :user;
\c :database :user

set client_min_messages = ERROR;
set client_encoding = 'LATIN1';

\i 1_schema.sql
\i 2_inserts.sql
\i 3_keys.sql

\set ECHO queries

\i 4_queries.sql
\i 5_constraints.sql
\i 6_rules.sql
\i 7_transactions.sql
