# CI/CD Deployment Prerequisites & Setup Guide

This guide explains exactly what you need to do before your GitHub Actions workflow (`release-deploy.yml`) will successfully run and deploy your application to EC2.

## Prerequisites

Before the automation can work, you must have the following prepared:

1. **An active AWS EC2 Instance**
   - Must have a public IP address or be accessible via SSH.
   - Must have an SSH `.pem` key that you use to log into it.
2. **Docker & Docker Compose installed on EC2**
   - The instance must have Docker installed.
   - The instance must have Docker Compose installed.
3. **The Initial Project Setup on EC2**
   - You must SSH into the server manually **one time** to set up the initial folder.
   - The `docker-compose.yml` file must be placed in `/home/ec2-user/Backend_Aws` on the server. (The `.env` file will be generated automatically by GitHub).
4. **A Docker Hub Account**
   - You need a free account on [Docker Hub](https://hub.docker.com/) to store your images.

---

## Step-by-Step Setup Guide

### 1. Set up the Initial Server State (One-time only)
SSH into your EC2 server and run the following:
```bash
# Create the directory the GitHub Action expects to find
mkdir -p /home/ec2-user/Backend_Aws

# (Optional) If your username is ubuntu instead of ec2-user, change the path above.
```
*Next, use SCP, Cyberduck, or FileZilla to copy your `docker-compose.yml` file into that `Backend_Aws` folder on the server.*

### 2. Configure GitHub Secrets
Your GitHub Action needs passwords and keys to push to Docker Hub and log into your server. You must hide these in GitHub Secrets.

1. Go to your repository on GitHub.
2. Click **Settings** (the gear icon).
3. On the left sidebar, expand **Secrets and variables**, then click **Actions**.
4. Click the green **New repository secret** button.

Add the following 5 secrets exactly as named:

| Secret Name | What to put in the "Secret" field |
| :--- | :--- |
| `DOCKER_USERNAME` | Your exact Docker Hub username (e.g., `vaishnav88sk`) |
| `DOCKER_PASSWORD` | **(Recommended)** A Docker Hub Personal Access Token (PAT) created in Docker Hub Account Settings > Security. *You can use your account password, but a PAT is more secure for CI/CD.* |
| `EC2_HOST` | The public IP address of your EC2 instance (e.g., `54.123.45.67`) |
| `EC2_USERNAME` | The username you use to SSH (usually `ubuntu` or `ec2-user`) |
| `EC2_SSH_KEY` | Open your `.pem` file in a text editor. Copy ALL the text (including `-----BEGIN RSA PRIVATE KEY-----` and the end line) and paste it here. |
| `PRODUCTION_ENV` | Copy the entire contents of your production `.env` file and paste it here. **Make sure it includes `LOG_LEVEL=INFO` (to prevent log bloat) and your `DOZZLE_USERNAME`/`DOZZLE_PASSWORD` credentials!** The workflow will automatically generate the `.env` file securely on your server before starting the app! |

### 3. How to Update Secrets or Environment Variables
If you ever need to change a value (for example, your EC2 IP address changes, or you need to add a new database password to `.env`), follow these steps:

1. Go to GitHub **Settings** > **Secrets and variables** > **Actions**.
2. Find the secret you want to change (e.g., `PRODUCTION_ENV` or `EC2_HOST`).
3. Click the **Pencil (Edit)** icon next to it.
4. Paste the new value and click **Update secret**.

> [!IMPORTANT]
> **Changing a secret does NOT automatically update your running server.** 
> For the new secret (like a new `.env` variable) to take effect, you **must trigger a new deployment** (as explained in step 4 below). The GitHub Action will run, read the newly updated secret, securely write the new `.env` file to your server, and restart the Docker containers to apply the changes.

### 4. How to Trigger a Deployment
Once the above is done, you never have to do it again. To deploy new code:

1. Write your code and push it to the `main` branch.
2. Go to your repository on GitHub.
3. Click on **Releases** (on the right side of the repository home page).
4. Click **Draft a new release**.
5. Click **Choose a tag**, type a version like `v1.0.0`, and click **Create new tag**.
6. Write a short title (e.g., "Initial Release").
7. Click the green **Publish release** button.

### What Happens Next?
The moment you click Publish, go to the **Actions** tab in GitHub. You will see your deployment workflow running! 
It will:
1. **Run Unit Tests:** Executes `mvn clean test`. If any test fails, deployment aborts immediately.
2. **Build Multi-Arch Docker Image:** Builds for both `linux/amd64` and `linux/arm64` using native host compilation (`--platform=$BUILDPLATFORM`) and GitHub Actions layer caching.
3. **Security Scan (Trivy):** Scans the built image for `CRITICAL` and `HIGH` vulnerabilities.
4. **Deploy to EC2 via SSH:**
   - Saves the current working container image ID (`PREV_IMAGE_ID`).
   - Pulls the newly pushed image.
   - Restarts containers using `docker compose up -d`.
   - Runs a live smoke test against `/actuator/health` (up to 60s).
   - **Automatic Rollback:** If the health check fails or times out, it automatically restores the previous working image and aborts the workflow.
   - Cleans up old dangling images (`docker image prune -f`).
   - Logs out of Docker Hub to remove cached credentials from disk.

---

## 🛠 Complete CI/CD Pipeline Reference

### 1. Workflows Overview

| Workflow File | Trigger | Purpose |
| :--- | :--- | :--- |
| **`release-deploy.yml`** | On GitHub Release Published | Tests, builds multi-arch image, scans with Trivy, deploys to EC2 with auto-rollback. |
| **`ci.yaml`** | Push & PR to `main` / `test` | Runs **in parallel**: Code Quality (PMD/SpotBugs/No-Emoji), Security Scan (CodeQL), and Build & Test (MySQL service container). |
| **`lint-pr.yml`** | PR opened / edited / synchronized | Enforces Conventional Commit PR titles (`feat:`, `fix:`, etc.) to guarantee release notes formatting. |
| **`auto-release-notes.yml`** | On GitHub Release Published | Auto-generates structured release notes categorized by commit types. |

---

### 2. Performance Optimizations (12 mins ➔ 3 mins)

* **Native Multi-Arch Build (`--platform=$BUILDPLATFORM`):**
  GitHub Actions runners are x86_64 (`amd64`). Compiling Java inside QEMU software emulation for ARM64 previously took ~8 minutes. By using `--platform=$BUILDPLATFORM` in the Dockerfile builder stage, Java compiles natively on the host runner at full speed, and the pre-built `.jar` is simply copied into both amd64 and arm64 Alpine runtime images in under 30 seconds.
* **Docker Layer Caching (`type=gha`):**
  Uses GitHub Actions cache to avoid re-downloading unchanged base image layers and Maven dependencies between release runs.
* **Parallel CI Jobs:**
  `build-and-test`, `code-quality`, and `security-scan` run simultaneously, providing test results within 2 minutes instead of waiting sequentially.
* **Concurrency (`cancel-in-progress: true`):**
  Automatically cancels older runs when rapid commits are pushed to the same branch or PR, saving runner quota.

---

### 3. Production Reliability Safeguards

* **Automatic Production Rollback:**
  Before swapping containers, the deployment script records the running image ID. If the new release fails to pass the live health check within 60 seconds (e.g. database connection error or bad config), it instantly re-tags and restarts the previous working version. Production is never left down.
* **Graceful Shutdown:**
  Configured via `server.shutdown=graceful` and `spring.lifecycle.timeout-per-shutdown-phase=20s` in `application.properties`. During container swaps, Spring Boot stops accepting new traffic and allows active in-flight requests up to 20 seconds to finish.
* **Container Healthcheck:**
  `docker-compose.yml` runs a lightweight healthcheck every 90 seconds against `/actuator/health` (with a 40-second startup grace period), allowing `docker ps` to display `Up (healthy)`.
* **Runtime Memory Tuning (`JAVA_OPTS`):**
  The Dockerfile entrypoint uses `exec java $JAVA_OPTS -jar app.jar`. Memory limits can be customized per-server in `.env` (e.g. `JAVA_OPTS=-Xmx768m`) without rebuilding the Docker image.

