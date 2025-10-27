#
# App: Customer Registration
# Package: docker.postgresql
# File: Dockerfile
# Version: 0.1.0
# Turns: 1
# Author: ChatGPT Codex
# Date: 2025-02-14T00:00:00Z
# Exports: postgres-container
# Description: Development PostgreSQL image with initialization hooks for Customer Registration.
#
FROM postgres:16-alpine

ENV POSTGRES_DB=${DATABASE_NAME:-customer_registration} \
    POSTGRES_USER=${DATABASE_USERNAME:-postgres} \
    POSTGRES_PASSWORD=${DATABASE_PASSWORD:-postgres}

COPY db /docker-entrypoint-initdb.d/db
COPY docker/postgresql/init-db.sh /docker-entrypoint-initdb.d/00-init.sh
