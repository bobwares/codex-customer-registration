-- App: Customer Registration
-- Package: com.bobwares.customer.registration
-- File: db/init/001_extensions.sql
-- Version: 0.1.0
-- Turns: 1
-- Author: Bobwares (bobwares@outlook.com)
-- Date: 2025-10-27T23:24:28Z
-- Exports: PostgreSQL extension bootstrap
-- Description: Ensures useful PostgreSQL extensions exist for UUID generation and cryptographic functions.
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS pgcrypto;
