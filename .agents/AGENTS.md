# Agent Customizations and Rules for Backend_Aws

This file contains important, project-specific rules that all agents MUST follow when working in this repository.

## 1. Commit Conventions
- You MUST use **Conventional Commits** for every single commit message (e.g., `feat:`, `fix:`, `chore:`, `docs:`, `refactor:`).
- *Why:* The GitHub Actions release notes pipeline (`auto-release-notes.yml`) automatically categorizes commits based on these prefixes. If you don't use them, the release notes will be broken.

## 2. Database Schema (Hibernate vs. Spring SQL Init)
- **NEVER** set `spring.jpa.hibernate.ddl-auto=update`. 
  - *Context:* We discovered that Hibernate 6 attempts to alter existing `varchar` column types during startup, which causes AWS RDS connection timeouts (`CommandAcceptanceException: This connection has been closed`).
- **ALWAYS** use `spring.jpa.hibernate.ddl-auto=validate`.
- If you need to create new tables, add the `CREATE TABLE IF NOT EXISTS` DDL to `src/main/resources/schema.sql`.
- Spring Boot is configured with `spring.sql.init.mode=always` and `spring.sql.init.continue-on-error=true` to automatically execute `schema.sql` on startup and safely ignore duplicate constraint errors.

## 3. Docker Compose & Networking
- **NEVER** remove `network_mode: "host"` from `docker-compose.yml`.
- Do not use standard port mappings (e.g., `ports: - "9090:9090"`). 
- *Context:* Host networking allows the container to natively bind to port 9090 and seamlessly connect to `localhost` databases when running locally, while also working flawlessly with AWS RDS when deployed on EC2.

## 4. Release and Deployment Pipeline
- Deployments to EC2 are fully automated. Do NOT attempt to manually SSH into the server to pull code or restart containers.
- The pipeline (`release-deploy.yml`) is triggered ONLY when a release is manually *published* on GitHub.
- If you push code, it will not deploy automatically. You must inform the user to go to the GitHub UI and publish a release.
