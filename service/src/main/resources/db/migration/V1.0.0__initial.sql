-- liquibase formatted sql

-- changeset sebastianstrucker:1736715607740-1
CREATE TABLE "ApiKeyEntity"
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name        VARCHAR(255)                            NOT NULL,
    value       VARCHAR(255)                            NOT NULL,
    "updatedAt" TIMESTAMP WITHOUT TIME ZONE,
    "createdAt" TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_apikeyentity PRIMARY KEY (id)
);

-- changeset sebastianstrucker:1736715607740-2
CREATE TABLE "EnvironmentEntity"
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    key         VARCHAR(255)                            NOT NULL,
    name        VARCHAR(255)                            NOT NULL,
    "updatedAt" TIMESTAMP WITHOUT TIME ZONE,
    "createdAt" TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_environmententity PRIMARY KEY (id)
);

-- changeset sebastianstrucker:1736715607740-3
CREATE TABLE "FeatureToggleEntity"
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    key         VARCHAR(255)                            NOT NULL,
    name        VARCHAR(255)                            NOT NULL,
    description VARCHAR(255),
    "updatedAt" TIMESTAMP WITHOUT TIME ZONE,
    "createdAt" TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_featuretoggleentity PRIMARY KEY (id)
);

-- changeset sebastianstrucker:1736715607740-4
ALTER TABLE "ApiKeyEntity"
    ADD CONSTRAINT uc_apikeyentity_value UNIQUE (value);

-- changeset sebastianstrucker:1736715607740-5
ALTER TABLE "EnvironmentEntity"
    ADD CONSTRAINT uc_environmententity_key UNIQUE (key);

-- changeset sebastianstrucker:1736715607740-6
ALTER TABLE "FeatureToggleEntity"
    ADD CONSTRAINT uc_featuretoggleentity_key UNIQUE (key);

