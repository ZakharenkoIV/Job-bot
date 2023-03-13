-- liquibase formatted sql

-- changeset Zakharenko:1
-- create table users
CREATE TABLE users
(
    id            BIGSERIAL PRIMARY KEY,
    username      VARCHAR(255)            NOT NULL,
    password      VARCHAR(255)            NOT NULL,
    last_activity TIMESTAMP DEFAULT now() NOT NULL
);