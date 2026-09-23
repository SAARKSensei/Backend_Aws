# Agent Customizations and Rules for Backend_Aws

This file contains critical, strict rules that all agents MUST follow when developing in this repository.

## 1. Local Development & Documentation
For any tasks related to local development setup, deployment, logging, or API testing, **you MUST read the corresponding documentation files before proceeding**:
- `README.md` (Main overview and Docker dev guide)
- `docs/RELEASE_PROCESS.md` (Release and auto-release-notes process)
- `docs/DEVELOPERS.md`, `docs/API_TESTING.md`, etc. (Check the Documentation Index in the README)

## 2. Commit Conventions
- You MUST use **Conventional Commits** for every single commit message (e.g., `feat:`, `fix:`, `chore:`, `docs:`, `refactor:`).
- *Why:* The automated release notes pipeline relies exclusively on these prefixes to categorize changes.

## 3. Database Schema (CRITICAL)
- **NEVER** set `spring.jpa.hibernate.ddl-auto=update`. 
- **ALWAYS** use `spring.jpa.hibernate.ddl-auto=validate`.
- *Context:* Hibernate attempts to alter existing `varchar` column types during startup, which causes AWS RDS connection timeouts (`CommandAcceptanceException: This connection has been closed`).
- If you need to create new tables, add the `CREATE TABLE IF NOT EXISTS` DDL to `src/main/resources/schema.sql`. Spring Boot is configured to safely execute this on startup and ignore duplicate constraint errors.

## 4. Docker Compose & Networking
- **NEVER** remove `network_mode: "host"` from `docker-compose.yml`.
- Do not use standard port mappings (e.g., `ports: - "9090:9090"`). 
- *Context:* Host networking allows the container to seamlessly connect to `localhost` databases when running locally, while also working flawlessly with AWS RDS when deployed.
