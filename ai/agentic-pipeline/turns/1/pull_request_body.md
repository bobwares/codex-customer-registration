<!--
PR TITLE (agent must set the GitHub PR title field to the following exact value):
Turn 1 – 2025-10-25 – 09:25:26
-->

## Turn Summary (from changelog.md)

<!-- CODEx_TURN_SUMMARY:BEGIN -->
Created the initial PostgreSQL migration and documentation for the CustomerProfile domain, enabling a normalized schema and supporting view for downstream Spring Boot integration.
<!-- CODEx_TURN_SUMMARY:END -->

## Input Prompt

Generate PostgreSQL DDL from the CustomerProfile persisted data schema in the session context.

## Tasks Executed

| Task Name | Tools / Agents Executed |
| --------- | ----------------------- |
| TASK - Generate Normalized Tables | manual-normalization |

## Turn Files Added (under /ai only)

| Path / File |
| ----------- |
| ai/agentic-pipeline/turns_index.csv |
| ai/agentic-pipeline/turns/1/template-changelog.md |
| ai/agentic-pipeline/turns/1/adr.md |
| ai/agentic-pipeline/turns/1/manifest.json |
| ai/agentic-pipeline/turns/1/pull_request_body.md |
| ai/agentic-pipeline/turns/1/session_context_values.md |

## Files Added (exclude /ai)

| Path / File | Task Name |
| ----------- | --------- |
| db/migrations/01_customer_profile_tables.sql | TASK - Generate Normalized Tables |
| db/README.md | TASK - Generate Normalized Tables |

## Files Updated (exclude /ai)

| Path / File | Task Name |
| ----------- | --------- |
| _None_ | |

## Checklist

- [ ] Unit tests pass
- [ ] Integration tests pass
- [ ] Linter passes
- [x] Documentation updated

## Codex Task Link
<leave blank>
