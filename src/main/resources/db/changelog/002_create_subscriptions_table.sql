-- liquibase formatted sql

-- changeset Zakharenko:2
-- create table subscriptions
CREATE TABLE subscriptions
(
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT                  NOT NULL,
    name       VARCHAR(255)            NOT NULL,
    settings   TEXT                    NOT NULL,
    created_at TIMESTAMP DEFAULT now() NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id)
);
