# 👨‍🔧 Maintainers Guide

This repository follows a structured CI/CD workflow with two protected branches: `test` and `main`.

---

## ✅ Branch Strategy

- **main**: Production-ready code. Merges into this branch deploy to production.
- **test**: Integration and staging environment. All PRs are first merged here for testing.

---

## 🔁 Maintainer Workflow

### 1. Review Contributor PRs → `test`

- Go to **Pull Requests**
- Review incoming PRs targeting `test` branch
- Validate:
    - Code style and logic
    - CI passes
    - Deployment (staging) functions as expected

> Optional: Test the deployed version in staging (EC2, Docker container, etc.)
---

### 2. Merge `test` → `main` for Production

Once tested and verified on `test`, do the following:

1. Open a PR from `test` → `main`
2. Add changelog or summary if needed
3. Review one more time (final QA)
4. Merge to `main` → this triggers **production deployment**

---

### 3. Visual Workflow:
```shell
your-username/forked-repo/your-branch ── PR ──▶ SAARKSensei/sensei-app-backend/test ──▶ QA / Staging
                                        │
                                Maintainer PR
                                        │
                                        ▼
                SAARKSensei/sensei-app-backend/main (Production)

```

## 🔐 Branch Protections

- ✅ PRs required for both `main` and `test`
- ✅ Status checks (e.g., tests, build) must pass
- ✅ Direct pushes are disabled

---

## 🚀 Deployment Triggers

| Branch | Action | Environment |
|--------|--------|-------------|
| `test` | Auto-deploy via CI/CD | **Staging / QA** |
| `main` | Auto-deploy via CI/CD | **Production** |

---

## 🛠 Tools Used

- **GitHub Actions** for CI/CD
- **Docker** for packaging
- **EC2** for deployment
- **SSH & DockerHub** for image transfer

---

## ✅ Good Practices

- Always squash and merge PRs for clean history.
- Label PRs (e.g., `feature`, `bugfix`, `docs`) before merging.
- Maintain changelogs if the project requires release notes.

---

