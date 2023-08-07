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
    file_store_type          VARCHAR(255) NOT NULL,
    upload_date        TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT file_log_generated_pk PRIMARY KEY (id),
    constraint storage_id_un unique (storage_id)
);

drop table if exists momentum_investments.file_store;

CREATE TABLE IF NOT EXISTS momentum_investments.file_store
(
    id                 UUID         NOT NULL,
    file_data BYTEA,
    creation_date      TIMESTAMP WITHOUT TIME ZONE,
    modified_date      TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT file_store_generated_pk PRIMARY KEY (id)
    );

    insert into momentum_investments.file_store (id, file_data, creation_date, modified_date) values ('ac137001-89af-1ac4-8189-afcb24440000', 'Momentum Investements, ,
Employee Name,Lusani Tshigabe
Position/Title,Senior Engineer
Department,Investments
Contact Number,0646578214
Email,TSHIGABEL@OUTLOOK.COM

Momentum Investements, ,
Employee Name,Lusani Tshigabe
Position/Title,Senior Engineer
Department,Investments
Contact Number,0646578214
Email,TSHIGABEL@OUTLOOK.COM
', '2023-08-01 08:31:13.231000', '2023-08-01 08:31:13.231000');
