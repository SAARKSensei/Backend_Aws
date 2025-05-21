# Contributing to Sensei Backend
---

## 📌 Table of Contents

- [Project Setup](#project-setup)
- [How to Contribute](#how-to-contribute)
- [Coding Guidelines](#coding-guidelines)
- [Git Guidelines](#git-guidelines)
- [Reporting Issues](#reporting-issues)

---

## Project Setup

1. Clone the repo:
```sh
   git clone https://github.com/SAARKSensei/sensei-app-backend.git
   cd sensei-app-backend
```

2. Build the project:
```sh
   ./mvnw clean package -DskipTests
```

3. Run locally with Docker:
   (prerequisites: Docker installed and enabled)
```sh
   docker build -t sensei-springboot-backend .
   docker run -p 9090:9090 --name sensei-springboot-app sensei-springboot-backend
```
Or without docker:
```shell
mvn spring-boot:run
```
Test on http://localhost:9090/api/.....

---

## How to Contribute

1. Fork the repository.
2. Create a new branch from `main`:

```sh
   git checkout -b your-branch-name
```

3. Sync your fork regularly:
```shell
    git remote add upstream https://github.com/SAARKSensei/sensei-app-backend.git
    git fetch upstream
    git checkout your-branch-name
    git merge upstream/your-branch-name
```

4. Make your changes and commit them to your branch.
5. Push to your fork:

```sh
   git push origin your-branch-name
```

6. Compare and Open a Pull Request on GitHub.\
Go to the original repo: 
- URL: `https://github.com/SAARKSensei/sensei-app-backend`
- Click `Contribute` → `Open pull request` \
**Then: Check all fields are same as below**
- Base repository: `SAARKSensei/sensei-app-backend`
- Base branch: `test` (Make sure this branch is `test` for testing is done before server push. If not, please select it from dropdown.)
- Head repository: `your-username/your-forked-repo-name`
- Compare: `your-branch`
- Submit the PR with a clear title and description.

Again sync your fork regularly.

---

## Coding Guidelines

- Use Java 11 (or higher if specified).
- Follow standard Spring Boot project structure.
- Keep code modular and well-commented.
- Include unit tests for any new logic.

---

## Git Guidelines

- Write clear and descriptive commit messages.
- Prefer multiple small commits over one large commit.
- Use specific commits for individual files or features:

  ```sh
  git add file.java
  git commit -m "Add validation to user service"
  ```

---

## Reporting Issues

If you find a bug or have a feature request:

- Use the [GitHub Issues](https://github.com/SAARKSensei/sensei-app-backend/issues) tab.
- Include as much detail as possible (logs, screenshots, steps to reproduce).

---

Thanks again for your interest in contributing to the Sensei backend! 🙌

