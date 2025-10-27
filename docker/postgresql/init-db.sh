#!/bin/bash
#
# App: Customer Registration
# Package: docker.postgresql
# File: init-db.sh
# Version: 0.1.0
# Turns: 1
# Author: ChatGPT Codex
# Date: 2025-02-14T00:00:00Z
# Exports: postgres-init
# Description: Applies schema migrations and seed data during container bootstrap.
#
set -euo pipefail

psql -v ON_ERROR_STOP=1 -d "$POSTGRES_DB" -U "$POSTGRES_USER" -f /docker-entrypoint-initdb.d/db/migrations/01_customer_profile_tables.sql
psql -v ON_ERROR_STOP=1 -d "$POSTGRES_DB" -U "$POSTGRES_USER" -f /docker-entrypoint-initdb.d/db/scripts/customer_profile_test_data.sql
