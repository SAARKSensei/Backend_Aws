# Internal Developer Guide (Sensei Backend)
---

Welcome to the internal team! This guide will help you set up your local environment and understand our development workflow for the Sensei Backend.

## 📌 Table of Contents

- [Project Setup](#project-setup)
- [Local Database Setup](#local-database-setup)
- [Internal Git Workflow](#internal-git-workflow)
- [Coding Guidelines](#coding-guidelines)
- [Reporting Issues](#reporting-issues)

---

## Project Setup

1. Clone the repository:
```sh
   git clone https://github.com/SAARKSensei/Backend_Aws.git
   cd Backend_Aws/
```

2. We use a `Makefile` to simplify common development commands. You can view all available commands by running:
```sh
   make help
```

### Available Makefile Commands:

**Java / Maven:**
- `make compile` - Compile the Java source code
- `make test` - Run the unit tests
- `make build` - Clean and package the application into a JAR
- `make run` - Run the Spring Boot application locally on port 9090
- `make clean` - Clean the target directory

**Docker:**
- `make docker-build` - Build the Docker image locally
- `make docker-up` - Spin up local Docker infrastructure (like Dozzle for logs)
- `make docker-down` - Tear down the Docker infrastructure
- `make docker-logs` - Tail the logs from Docker containers

*Example workflow:*
```sh
# 1. Start your database and logging containers
make docker-up

# 2. Run your application locally
make run
```
Test on http://localhost:9090/api/.....

---

## Local Database Setup

### 1. Create the Local MySQL Database
Before running the application, you must set up the local MySQL database.
1. Connect to your local MySQL instance:
   ```sh
   mysql -u root -p
   ```
2. Create the empty database:
   ```sql
   CREATE DATABASE IF NOT EXISTS sensei_db;
   ```
3. *Note on Tables:* You do **not** need to create tables manually. When you start the Spring Boot application (or run Docker), it will automatically generate all the necessary tables based on our `schema.sql`.

### 2. Seeding Dummy Data (Optional)
If this is your first time setting up and you want to test the APIs quickly, you can populate your local database with dummy data (Subjects, Modules, Pricing Plans, Users).
**After the application has started at least once** (so the tables exist), run the provided dummy data script:
```sh
mysql -u root -p sensei_db < scripts/dummy_data.sql
```

---

## Internal Git Workflow

Because this is a private, internal repository, **do not fork** the project. We work directly on feature branches within the main repository.

1. Ensure you are on the `main` branch and pull the latest changes:
   ```sh
   git checkout main
   git pull origin main
   ```

2. Create a new branch for your feature or bugfix. **Please include your name or initials in the branch name** so we can easily track who is working on what:
   ```sh
   git checkout -b feature/john-new-payment-endpoint
   # or bugfix/john-fix-login-crash
   ```

3. Make your changes and commit them with descriptive messages. **You MUST use Conventional Commits** for every single commit message.
   > [!IMPORTANT]
   > Our automated release notes pipeline relies exclusively on these prefixes to categorize changes!
   
   **Allowed Prefixes & Pipeline Categories:**
   
   **✨ Key Features**
   - `feat:` - A new feature (e.g., adding a new API endpoint or table)
   
   **🐛 Bug Fixes & Deployment Hotfixes**
   - `fix:` - A bug fix (e.g., resolving a logic error)
   
   **🏗 Architecture & Refactoring**
   - `refactor:` - A code change that neither fixes a bug nor adds a feature
   - `perf:` - A code change that improves performance
   
   **📚 DevOps & Documentation**
   - `build:` - Changes that affect the build system or external dependencies (Maven, Docker)
   - `ci:` - Changes to our CI configuration files and scripts (GitHub Actions)
   - `docs:` - Documentation only changes (e.g., updating README or DEVELOPERS guide)
   - `test:` - Adding missing tests or correcting existing tests
   - `chore:` - Minor maintenance tasks or auxiliary tool changes
   - `style:` - Changes that do not affect the meaning of the code (formatting, etc.)

   ```sh
   git add .
   git commit -m "feat: add new payment verification endpoint"
   ```

4. Push your branch to the remote repository:
   ```sh
   git push origin feature/your-feature-name
   ```

5. Open a Pull Request on GitHub:
   - Base branch: **`main`** (Ensure all PRs target the `main` branch).
   - Compare branch: `feature/john-new-payment-endpoint`
   - Submit the PR with a clear title and description.

---

## Coding Guidelines

- Use Java 25.
- Follow standard Spring Boot project structure.
- Keep code modular and well-commented.
- Include unit tests for any new logic.

---

## Reporting Issues

If you find a bug or have a feature request, please use the internal issue tracker. Provide as much detail as possible (logs, screenshots, steps to reproduce).

---

Thanks for contributing to the Sensei backend! 🙌
