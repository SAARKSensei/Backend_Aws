# 👨‍🔧 Maintainers Guide

This repository follows a structured CI/CD workflow. It is an internal, private repository where all developers work on feature branches and create Pull Requests directly against the `main` branch.

---

## ✅ Branch Strategy

- **main**: Production-ready code. Merges into this branch automatically deploy to production.
- **feature/* or bugfix/* **: Temporary branches where developers work on new features and fixes before opening a PR to `main`.

---

## 🔁 Maintainer Workflow

### 1. Review Internal PRs → `main`

- Go to **Pull Requests**
- Review incoming PRs targeting the `main` branch.
- Validate:
    - Code style and logic
    - **Commit messages** strictly follow Conventional Commits (e.g. `feat:`, `fix:`)
    - CI tests pass
- If everything looks good, approve and merge the PR.

### 2. Auto-Release Notes and Deployment

- Merging into `main` **does not** automatically trigger production deployment.
- To trigger deployment to the EC2 production servers, the maintainer must **publish a new GitHub Release**. (For step-by-step instructions, please see [RELEASE_PROCESS.md](./RELEASE_PROCESS.md)).
- Publishing a Release will trigger both `release-deploy.yml` (for production deployment) and `auto-release-notes.yml` (which automatically generates the changelog based on Conventional Commits prefixes). There is no need to maintain manual changelogs.

---

## 🔐 Branch Protections

- ✅ PRs required for `main`
- ✅ Status checks (e.g., tests, build) must pass before merging
- ✅ Direct pushes to `main` are disabled

---

## 🚀 Deployment Triggers

| Branch | Action | Environment |
|--------|--------|-------------|
| `main` | Publish GitHub Release | **Production** |

---

## 🛠 Tools Used

- **GitHub Actions** for CI/CD and Release Notes
- **Docker** for packaging
- **EC2** for deployment
- **SSH & DockerHub** for image transfer

---

## ✅ Good Practices

- **Squash and merge** PRs for clean history.
- Enforce **Conventional Commits** strictly. The release notes pipeline will break or ignore commits if they don't follow the prefixes!
- Ensure developers include their names in branch names (e.g., `feature/john-api`).

---
