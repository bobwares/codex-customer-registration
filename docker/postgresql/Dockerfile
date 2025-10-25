# App: Customer Registration
# Package: docker/postgresql
# File: Dockerfile
# Version: 0.1.0
# Turns: 1
# Author: AI Agent
# Date: 2025-10-25T09:49:02Z
# Exports: Docker image definition for PostgreSQL
# Description: Provides a PostgreSQL 16 image with initialized schemas and seed directories for local development.

FROM postgres:16-alpine

LABEL org.opencontainers.image.title="customer-registration-postgresql" \
      org.opencontainers.image.description="PostgreSQL image for the Customer Registration service" \
      org.opencontainers.image.version="0.1.0"

RUN mkdir -p /docker-entrypoint-initdb.d/migrations \
    && mkdir -p /docker-entrypoint-initdb.d/scripts

COPY db/migrations /docker-entrypoint-initdb.d/migrations
COPY db/scripts /docker-entrypoint-initdb.d/scripts

