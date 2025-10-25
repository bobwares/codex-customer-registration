/*
 * App: Customer Registration
 * Package: db
 * File: 01_customer_profile_tables.sql
 * Version: 0.1.0
 * Turns: Turn 1
 * Author: gpt-5-codex
 * Date: 2025-10-25T09:25:26Z
 * Exports: DDL statements for customer profile tables, indexes, and views
 * Description: Creates the customer_profile schema, normalized tables for customer data, supporting indexes, and a reporting view.
 */

BEGIN;

-- Ensure dedicated schema exists
CREATE SCHEMA IF NOT EXISTS customer_profile;

-- Ensure all subsequent statements target the domain schema
SET search_path TO customer_profile, public;

/* ---------- Reference tables ---------- */
CREATE TABLE IF NOT EXISTS postal_address (
    address_id      BIGSERIAL PRIMARY KEY,
    line1           VARCHAR(255) NOT NULL,
    line2           VARCHAR(255),
    city            VARCHAR(120) NOT NULL,
    state           VARCHAR(80) NOT NULL,
    postal_code     VARCHAR(20),
    country         CHAR(2) NOT NULL,
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS privacy_settings (
    privacy_settings_id  BIGSERIAL PRIMARY KEY,
    marketing_emails_enabled BOOLEAN NOT NULL,
    two_factor_enabled       BOOLEAN NOT NULL,
    created_at               TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at               TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

/* ---------- Root entity ---------- */
CREATE TABLE IF NOT EXISTS customer (
    customer_id         UUID PRIMARY KEY,
    first_name          VARCHAR(255) NOT NULL,
    middle_name         VARCHAR(255),
    last_name           VARCHAR(255) NOT NULL,
    address_id          BIGINT REFERENCES postal_address(address_id) ON DELETE SET NULL,
    privacy_settings_id BIGINT REFERENCES privacy_settings(privacy_settings_id) ON DELETE SET NULL,
    created_at          TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_customer_address_id
    ON customer (address_id);
CREATE INDEX IF NOT EXISTS idx_customer_privacy_settings_id
    ON customer (privacy_settings_id);
CREATE INDEX IF NOT EXISTS idx_customer_last_name
    ON customer (last_name);

/* ---------- One-to-many collections ---------- */
CREATE TABLE IF NOT EXISTS customer_email (
    email_id        BIGSERIAL PRIMARY KEY,
    customer_id     UUID NOT NULL REFERENCES customer(customer_id) ON DELETE CASCADE,
    email           VARCHAR(320) NOT NULL,
    is_primary      BOOLEAN NOT NULL DEFAULT FALSE,
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE UNIQUE INDEX IF NOT EXISTS uq_customer_email
    ON customer_email (customer_id, LOWER(email));

CREATE INDEX IF NOT EXISTS idx_customer_email_customer_id
    ON customer_email (customer_id);
CREATE INDEX IF NOT EXISTS idx_customer_email_address
    ON customer_email (LOWER(email));

CREATE TABLE IF NOT EXISTS customer_phone_number (
    phone_id        BIGSERIAL PRIMARY KEY,
    customer_id     UUID NOT NULL REFERENCES customer(customer_id) ON DELETE CASCADE,
    type            VARCHAR(20) NOT NULL,
    number          VARCHAR(20) NOT NULL,
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE UNIQUE INDEX IF NOT EXISTS uq_customer_phone
    ON customer_phone_number (customer_id, number);
CREATE INDEX IF NOT EXISTS idx_customer_phone_customer_id
    ON customer_phone_number (customer_id);
CREATE INDEX IF NOT EXISTS idx_customer_phone_number
    ON customer_phone_number (number);

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'chk_customer_phone_type'
          AND conrelid = 'customer_phone_number'::regclass
    ) THEN
        ALTER TABLE customer_phone_number
            ADD CONSTRAINT chk_customer_phone_type CHECK (type IN ('mobile', 'home', 'work', 'other'));
    END IF;
END
$$;

/* ---------- Flattened view for reporting ---------- */
CREATE OR REPLACE VIEW customer_profile_flat_view AS
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
    COALESCE(
        ARRAY_AGG(DISTINCT LOWER(ce.email))
            FILTER (WHERE ce.email IS NOT NULL),
        ARRAY[]::TEXT[]
    ) AS emails,
    COALESCE(
        JSONB_AGG(DISTINCT jsonb_build_object('type', cpn.type, 'number', cpn.number))
            FILTER (WHERE cpn.phone_id IS NOT NULL),
        '[]'::JSONB
    ) AS phone_numbers
FROM customer c
LEFT JOIN postal_address pa ON pa.address_id = c.address_id
LEFT JOIN privacy_settings ps ON ps.privacy_settings_id = c.privacy_settings_id
LEFT JOIN customer_email ce ON ce.customer_id = c.customer_id
LEFT JOIN customer_phone_number cpn ON cpn.customer_id = c.customer_id
GROUP BY
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
    ps.two_factor_enabled;

COMMIT;
