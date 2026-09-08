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
   - The `docker-compose.yml` file must be placed in `/home/<your-ec2-username>/Backend_Aws` on the server. (The `.env` file will be generated automatically by GitHub).
4. **A Docker Hub Account**
   - You need a free account on [Docker Hub](https://hub.docker.com/) to store your images.

---

## Step-by-Step Setup Guide

### 1. Set up the Initial Server State (One-time only)
SSH into your EC2 server and run the following:
```bash
# Create the directory the GitHub Action expects to find
mkdir -p /home/ubuntu/Backend_Aws

# (Optional) If your username is ec2-user instead of ubuntu, change the path above.
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
| `DOCKER_USERNAME` | Your exact Docker Hub username (e.g., `vaishnavsk`) |
| `DOCKER_PASSWORD` | Your Docker Hub password (or an Access Token created in Docker Hub settings) |
| `EC2_HOST` | The public IP address of your EC2 instance (e.g., `54.123.45.67`) |
| `EC2_USERNAME` | The username you use to SSH (usually `ubuntu` or `ec2-user`) |
| `EC2_SSH_KEY` | Open your `.pem` file in a text editor. Copy ALL the text (including `-----BEGIN RSA PRIVATE KEY-----` and the end line) and paste it here. |
| `PRODUCTION_ENV` | Copy the entire contents of your production `.env` file and paste it here. **Make sure it includes `LOG_LEVEL=INFO` (to prevent log bloat) and your `DOZZLE_USERNAME`/`DOZZLE_PASSWORD` credentials!** The workflow will automatically generate the `.env` file securely on your server before starting the app! |

### 3. How to Trigger a Deployment
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
1. Run `mvn test` (If tests fail, it turns red and stops immediately).
2. Build your Docker image and push it to Docker Hub.
3. Securely SSH into your EC2 instance.
4. Pull the new image and restart the containers silently.

You can then open `http://<EC2-IP>:9999` to watch your live logs in Dozzle!
