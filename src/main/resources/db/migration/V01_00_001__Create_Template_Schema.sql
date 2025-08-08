CREATE SCHEMA IF NOT EXISTS template_data AUTHORIZATION postgres;

CREATE TYPE TEMPLATE_TYPE AS ENUM ('TYPE_A', 'TYPE_B', 'TYPE_C');

CREATE TABLE template_table
(
    id                UUID             NOT NULL
        CONSTRAINT template_pkey PRIMARY KEY,
    name              VARCHAR(255)     NOT NULL,
    registration_id   VARCHAR(255)     NOT NULL,
    manufacturer      VARCHAR(255)     NOT NULL,
    model             VARCHAR(255)     NOT NULL,
    type              TEMPLATE_TYPE_TYPE    NOT NULL,
    create_user_id    VARCHAR          NOT NULL,
    create_dtime      TIMESTAMPTZ      NOT NULL,

    UNIQUE (registration_id)
);

