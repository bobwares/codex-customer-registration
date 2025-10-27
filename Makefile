#
# App: Customer Registration
# Package: root
# File: Makefile
# Version: 0.1.0
# Turns: 1
# Author: ChatGPT Codex
# Date: 2025-02-14T00:00:00Z
# Exports: make-targets
# Description: Developer automation for database lifecycle and docker orchestration.
#
SHELL := /bin/bash

include .env.postgresql

PROFILE ?= postgresql
DATABASE_URL ?= postgres://$(DATABASE_USERNAME):$(DATABASE_PASSWORD)@$(DATABASE_HOST):$(DATABASE_PORT)/$(DATABASE_NAME)

.PHONY: up down logs migrate seed psql status

up:
@echo "Starting $(PROFILE) services..."
docker compose --profile $(PROFILE) up -d

status:
docker compose ps

logs:
docker compose logs -f

migrate:
@[ -f db/migrations/01_customer_profile_tables.sql ]
psql "$(DATABASE_URL)" -f db/migrations/01_customer_profile_tables.sql

seed:
@[ -f db/scripts/customer_profile_test_data.sql ]
psql "$(DATABASE_URL)" -f db/scripts/customer_profile_test_data.sql

psql:
psql "$(DATABASE_URL)"

down:
@echo "Stopping $(PROFILE) services..."
docker compose --profile $(PROFILE) down --remove-orphans
