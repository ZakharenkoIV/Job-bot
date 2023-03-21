--liquibase formatted sql

--changeset Zakharenko:4
--add new columns to users table
ALTER TABLE users
    ADD COLUMN telegram_id BIGINT;
ALTER TABLE users
    ADD COLUMN firstname VARCHAR(255);
ALTER TABLE users
    ADD COLUMN lastname VARCHAR(255);
ALTER TABLE users
    ADD COLUMN language_code VARCHAR(10);
ALTER TABLE users
    DROP COLUMN password;
ALTER TABLE users
    DROP COLUMN last_activity;
ALTER TABLE users
    ADD CONSTRAINT unique_telegram_id UNIQUE (telegram_id);