# 🧠 Sensei App Backend

Spring Boot backend service for the **Sensei** .

---

## 🚀 Features

- REST APIs
- Dockerized architecture
- CI/CD pipeline with GitHub Actions
- Auto-deployment to EC2 (Amazon Linux)

---

## 📚 Documentation Index

We have several documentation files to help you navigate, test, and deploy the system. Click on any document to read it:

| Document | Purpose |
| :--- | :--- |
| **[README.md](./README.md)** | Main project overview and Docker deployment guide |
| **[CONTRIBUTING.md](./CONTRIBUTING.md)** | Guidelines for developers contributing to the project |
| **[MAINTAINERS.md](./MAINTAINERS.md)** | Guidelines and responsibilities for code owners/maintainers |
| **[API_TESTING.md](./API_TESTING.md)** | Comprehensive list of active API endpoints for testing via Postman/cURL |
| **[AUTHENTICATION_FLOW.md](./AUTHENTICATION_FLOW.md)** | Documentation on how JWT and Google OAuth authentication works |
| **[FRONTEND_API_MIGRATION.md](./FRONTEND_API_MIGRATION.md)** | Guide for frontend devs on integrating with backend API changes |
| **[LOGGING_GUIDE.md](./LOGGING_GUIDE.md)** | Best practices and standards for application logging |
| **[CI_CD_SETUP_GUIDE.md](.github/workflows/CI_CD_SETUP_GUIDE.md)** | Detailed guide on configuring GitHub Actions and Docker Hub for automated EC2 deployments |

---

## 🐳 Docker Deployment Guide

The application uses a unified `docker-compose.yml` that seamlessly supports both Local Development and AWS EC2 environments natively, thanks to host networking.

### Local Development (Physical Database)
When running locally, the application connects to the MySQL instance physically installed on your machine.
1. Ensure your `.env` has `SPRING_PROFILES_ACTIVE=dev`
2. Start the application:
   ```sh
   make docker-up
   ```
*Note: Because `docker-compose.yml` uses `network_mode: "host"`, the container's `localhost` perfectly maps to your physical machine's `localhost`. It will securely connect to your physical MySQL database without requiring any OS-level `bind-address` config changes.*

### Production (AWS EC2 & RDS)
When running in production on EC2, the application connects to a remote AWS RDS instance.
1. Ensure your `.env` has `SPRING_PROFILES_ACTIVE=prod` and the correct `DB_URL` pointing to your RDS instance.
2. Start the application:
   ```sh
   make docker-up
   ```
*Note: `network_mode: "host"` behaves exactly like port mapping (`-p 9090:9090`) in EC2, directly exposing the application on port 9090 to the internet/load balancer while securely communicating with RDS over the VPC.*

---

### Sensei - A product by SAARK Edu. Pvt. Ltd