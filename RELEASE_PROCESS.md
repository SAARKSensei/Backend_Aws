# 🚀 Release & Deployment Process

This document outlines how to trigger a new release, generate beautifully formatted release notes, and deploy the application to the AWS EC2 server.

## 1. Overview of the CI/CD Pipeline

Our deployment pipeline is highly automated but triggered **manually** via the GitHub interface to give you full control.

When you create and publish a new Release on GitHub:
1. The **Update Release Notes** action (`auto-release-notes.yml`) intercepts it. It scans your commit history and automatically populates the empty release body with beautifully categorized notes based on your conventional commits.
2. The **Production Release & Deploy** action (`release-deploy.yml`) is triggered simultaneously. It pulls your code, runs tests, builds the Docker image, pushes it to Docker Hub, and safely restarts the container on EC2.

---

## 2. Versioning Strategy (SemVer)

We follow [Semantic Versioning](https://semver.org/) for our release tags. Every release must be tagged in the format `vMAJOR.MINOR.PATCH` (e.g., `v1.2.3`).

- **MAJOR** version (`vX.0.0`): Increment when you make incompatible or breaking API changes (e.g., removing an endpoint frontend relies on).
- **MINOR** version (`v0.X.0`): Increment when you add new functionality in a backwards-compatible manner (e.g., adding a new ParentQuiz API).
- **PATCH** version (`v0.0.X`): Increment when you make backwards-compatible bug fixes (e.g., fixing a database connection issue).

*Note: The `v` prefix is required (e.g. `v0.0.4`, not `0.0.4`).*

---

## 3. How to Publish a Release

To push a new version of the code to production, follow these steps exactly:

### Step 1: Ensure code is on `main`
Ensure all your final changes are committed and pushed to the `main` branch. 
*Note: Make sure to use conventional commit prefixes (e.g., `feat:`, `fix:`, `chore:`) so the notes generator can categorize them!*

### Step 2: Go to the GitHub Releases Page
1. Open your repository on GitHub.
2. On the right-hand sidebar, click on **Releases**.
3. Click the **"Draft a new release"** button.

### Step 3: Fill in the Version
1. Click **"Choose a tag"**.
2. Type in your new version number (e.g., `v0.0.3`) and click **"Create new tag: v0.0.3 on publish"**.
3. *Important:* Leave the "Release title" and the large description text box **completely empty**.

### Step 4: Publish
Click the big green **"Publish release"** button at the bottom!

---

## 4. What happens next? (Magic!)

The moment you click Publish, you can go to the **Actions** tab on GitHub to watch the magic happen:

1. **Auto-Generate Notes**: A workflow will run and instantly overwrite the empty release description you just published with a stunning, categorized changelog based on all your commits since the last tag.
2. **Deploy to EC2**: The deployment pipeline will begin. It will:
   - Run Maven tests
   - Build a multi-architecture Docker image (`amd64` and `arm64`)
   - Push to Docker Hub
   - SSH into EC2, pull the image, and run `docker compose up -d` to smoothly swap the old container for the new one without any port conflicts.

---

## 5. Commit Conventions (Crucial for Tracking!)

To ensure that the automated release notes generator can successfully track and categorize changes, **all developers must use Conventional Commits**. If a commit does not follow this format, it will be dumped into an "Uncategorized" section in the release notes.

### Format
`type: Short description of the change`
*(Example: `feat: Added new Razorpay payment gateway`)*

### Allowed Types & How They Are Categorized

When you publish a release, the generator reads your commit prefixes and automatically sorts them into these specific headings:

| Commit Prefix | When to use it | Heading in Release Notes |
| :--- | :--- | :--- |
| `feat:` / `feature:` | Adding a brand new feature or API endpoint | **✨ Key Features** |
| `fix:` / `bug:` | Fixing a bug, crash, or hotfixing a deployment | **🐛 Bug Fixes & Deployment Hotfixes** |
| `refactor:` / `perf:` | Restructuring code without changing behavior, or improving performance | **🏗 Architecture & Refactoring** |
| `docs:` | Changing documentation (README, etc.) | **📚 DevOps & Documentation** |
| `chore:` | Routine tasks, dependency updates, fixing typos | **📚 DevOps & Documentation** |
| `test:` | Adding or fixing unit tests | **📚 DevOps & Documentation** |
| `style:` | Formatting changes (spaces, commas) | **📚 DevOps & Documentation** |
| `ci:` | Changes to GitHub Actions or deployment scripts | **📚 DevOps & Documentation** |

**Golden Rule:** Always start your commit message with one of these prefixes!
