-- liquibase formatted sql

-- changeset Zakharenko:3
-- create table jobs
CREATE TABLE jobs
(
    id              BIGSERIAL PRIMARY KEY,
    subscription_id BIGINT                  NOT NULL,
    message         TEXT                    NOT NULL,
    date            TIMESTAMP               NOT NULL,
    hash            VARCHAR(64)             NOT NULL,
    FOREIGN KEY (subscription_id) REFERENCES subscriptions (id)
);