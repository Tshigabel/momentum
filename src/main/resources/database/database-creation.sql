CREATE SCHEMA IF NOT EXISTS momentum_investments;

CREATE TABLE IF NOT EXISTS momentum_investments.file_log
(
    id                 UUID         NOT NULL,
    original_file_name VARCHAR(255),
    creation_date      TIMESTAMP WITHOUT TIME ZONE,
    modified_date      TIMESTAMP WITHOUT TIME ZONE,
    checksum           VARCHAR(255),
    storage_id        VARCHAR(255) unique,
    file_type          VARCHAR(255) NOT NULL,
    upload_date        TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT file_log_generated_pk PRIMARY KEY (id),
    constraint storage_id_un unique (storage_id)
);
