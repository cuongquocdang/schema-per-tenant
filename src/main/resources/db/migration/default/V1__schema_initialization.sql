-- SCHEMA
CREATE SCHEMA IF NOT EXISTS saas;

-- TABLES
CREATE TABLE saas.data_source_configs
(
    id           BIGSERIAL,
    name         VARCHAR(255) NOT NULL UNIQUE,
    database_url VARCHAR(255) NOT NULL UNIQUE,
    username     VARCHAR(255),
    password     VARCHAR(255),
    initialize   BOOLEAN,
    created_at   TIMESTAMP,
    created_by   VARCHAR(255),
    updated_at   TIMESTAMP,
    updated_by   VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE saas.tenants
(
    id                   BIGSERIAL,
    name                 VARCHAR(100),
    schema_name          VARCHAR(255),
    databaseUrl          VARCHAR(255),
    datasource_config_id BIGINT NOT NULL,
    created_at           TIMESTAMP,
    created_by           VARCHAR(255),
    updated_at           TIMESTAMP,
    updated_by           VARCHAR(255),
    PRIMARY KEY (id)
);


-- CONSTRAINTS
ALTER TABLE saas.data_source_configs
    ADD CONSTRAINT "data_source_configs_name_unique_key" UNIQUE ("name");

ALTER TABLE saas.tenants
    ADD CONSTRAINT "tenants_name_unique_key" UNIQUE ("name");

ALTER TABLE saas.tenants
    ADD CONSTRAINT FK_tenants_datasource_config_id FOREIGN KEY (datasource_config_id) REFERENCES saas.data_source_configs (id);