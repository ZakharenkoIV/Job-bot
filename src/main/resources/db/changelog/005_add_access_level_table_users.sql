--liquibase formatted sql

--changeset Zakharenko:5
--add new column to users table
ALTER TABLE users
    ADD COLUMN access_level VARCHAR(255);
ALTER TABLE users
    ADD COLUMN telegram_chat_id BIGINT;