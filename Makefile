# App: Customer Registration
# Package: build
# File: Makefile
# Version: 0.1.0
# Turns: 1
# Author: AI Agent
# Date: 2025-10-25T09:49:02Z
# Exports: Local automation commands
# Description: Provides shortcuts for managing Docker services, migrations, seeds, and smoke-test queries.

ENV_FILE ?= .env.postgresql

ifneq ("$(wildcard $(ENV_FILE))","")
include $(ENV_FILE)
export $(shell sed -n 's/^[[:space:]]*\([^#=][^=]*\)=.*/\1/p' $(ENV_FILE))
endif

COMPOSE := docker compose --env-file $(ENV_FILE) --profile postgresql
PSQL := $(COMPOSE) exec -T postgresql-db psql -v ON_ERROR_STOP=1 -U $(DB_USERNAME) -d $(DB_NAME)

.PHONY: docker-up docker-down db-migrate db-seed db-query

docker-up:
	@echo "Starting PostgreSQL services..."
	$(COMPOSE) up -d postgresql-db

docker-down:
	@echo "Stopping PostgreSQL services..."
	$(COMPOSE) down

db-migrate: docker-up
	@echo "Applying database migrations..."
	$(PSQL) -f /docker-entrypoint-initdb.d/migrations/01_customer_profile_tables.sql

db-seed: db-migrate
	@echo "Seeding test data..."
	$(PSQL) -f /docker-entrypoint-initdb.d/scripts/customer_profile_test_data.sql

db-query: db-seed
	@echo "Running smoke-test query..."
	$(PSQL) -c "SELECT COUNT(*) AS customer_count FROM customer_registration.customer;"

