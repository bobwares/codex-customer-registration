# Turn 1 — Changelog
Date (UTC): 2025-09-12T16:28:01Z
Task: create_app
Scope: customer

## Summary
Initial application setup with persistence and REST layers.

## Changes
- Added Maven Spring Boot skeleton and application class.
- Generated customer domain entities, repository, service, and controller.
- Created PostgreSQL migration and seed data scripts.
- Added tests and e2e HTTP script.

## Migrations
- db/migrations/01_customer_tables.sql

## SemVer Impact
- Minor: initial features added.

## Risks & Mitigations
- Complex mappings may require further validation; tests provide coverage.

## Linked Artifacts
- ADR: ./adr.md
- Diff: ./diff.patch
- Manifest: ./manifest.json
