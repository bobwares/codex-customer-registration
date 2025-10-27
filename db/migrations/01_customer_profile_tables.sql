/*
 * App: Customer Registration
 * Package: db.migrations
 * File: 01_customer_profile_tables.sql
 * Version: 0.1.0
 * Turns: 1
 * Author: ChatGPT Codex
 * Date: 2025-02-14T00:00:00Z
 * Exports: customer_profile_tables
 * Description: Creates normalized tables, relationships, and supporting objects for the Customer Profile domain.
 */
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TYPE IF NOT EXISTS phone_type AS ENUM ('mobile', 'home', 'work', 'other');

CREATE TABLE IF NOT EXISTS postal_address (
    address_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    line1 VARCHAR(255) NOT NULL,
    line2 VARCHAR(255),
    city VARCHAR(100) NOT NULL,
    state VARCHAR(100) NOT NULL,
    postal_code VARCHAR(20) NOT NULL,
    country CHAR(2) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS privacy_settings (
    privacy_settings_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    marketing_emails_enabled BOOLEAN NOT NULL,
    two_factor_enabled BOOLEAN NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS customer (
    customer_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    first_name VARCHAR(100) NOT NULL,
    middle_name VARCHAR(100),
    last_name VARCHAR(100) NOT NULL,
    address_id BIGINT NOT NULL,
    privacy_settings_id BIGINT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_customer_address FOREIGN KEY (address_id) REFERENCES postal_address(address_id) ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_customer_privacy FOREIGN KEY (privacy_settings_id) REFERENCES privacy_settings(privacy_settings_id) ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE UNIQUE INDEX IF NOT EXISTS ux_customer_address_id ON customer(address_id);
CREATE UNIQUE INDEX IF NOT EXISTS ux_customer_privacy_settings_id ON customer(privacy_settings_id);

CREATE TABLE IF NOT EXISTS customer_email (
    email_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    customer_id UUID NOT NULL,
    email VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_customer_email_customer FOREIGN KEY (customer_id) REFERENCES customer(customer_id) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT uq_customer_email UNIQUE (customer_id, email)
);

CREATE TABLE IF NOT EXISTS customer_phone_number (
    phone_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    customer_id UUID NOT NULL,
    type phone_type NOT NULL,
    number VARCHAR(20) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_customer_phone_customer FOREIGN KEY (customer_id) REFERENCES customer(customer_id) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT uq_customer_phone UNIQUE (customer_id, number)
);

CREATE INDEX IF NOT EXISTS ix_customer_email_customer_id ON customer_email(customer_id);
CREATE INDEX IF NOT EXISTS ix_customer_email_email ON customer_email(email);
CREATE INDEX IF NOT EXISTS ix_customer_phone_customer_id ON customer_phone_number(customer_id);
CREATE INDEX IF NOT EXISTS ix_customer_phone_type ON customer_phone_number(type);

CREATE OR REPLACE VIEW customer_profile_view AS
SELECT
    c.customer_id,
    c.first_name,
    c.middle_name,
    c.last_name,
    pa.line1,
    pa.line2,
    pa.city,
    pa.state,
    pa.postal_code,
    pa.country,
    ps.marketing_emails_enabled,
    ps.two_factor_enabled,
    ARRAY(SELECT ce.email FROM customer_email ce WHERE ce.customer_id = c.customer_id ORDER BY ce.email) AS emails,
    ARRAY(SELECT jsonb_build_object('type', cpn.type, 'number', cpn.number) FROM customer_phone_number cpn WHERE cpn.customer_id = c.customer_id ORDER BY cpn.type) AS phone_numbers
FROM customer c
JOIN postal_address pa ON pa.address_id = c.address_id
JOIN privacy_settings ps ON ps.privacy_settings_id = c.privacy_settings_id;

COMMENT ON VIEW customer_profile_view IS 'Flattened projection combining customer identity, address, privacy, emails, and phones.';
