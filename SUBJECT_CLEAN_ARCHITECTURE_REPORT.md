# Subject Module - Clean Architecture Migration Report

## 1. Purpose of This Report

This document explains:

- What problems existed in the Subject module earlier
- Why certain files(ports, adapters, repositories) are required
- What issues occurred during the migration
- How those issues were resolved
- How the **Subject module now works end-to-end** using Clean Architecture.
- How to explain this clearly to a manager / stakeholder.

---

## 2. Initial Problem Statment

Originally, the Subject module mixed multiple responsibilities in the same classes:

- Controllers called services directly
- Services contained:
  - Business logic
  - DataBase logic
  - DTO mapping
- JPA entities were treated as business models.
- Framework(Spring/JPA) details leaked into business logic

This caused:

- Tight coupling
- Hard-to-test code
- Difficult future refactoring
- Confusion when introducing new architecture

---

## 3. Clean Architecture Goal

The migration aimed to enforce **Clean Architecture**, whose core rule is:

> **Business logic must not depend on frameworks or infrastructure.**

This led to a layered structure:

Controller → Use Case → Domain → Port
↓
Adapter → JPA Repository → Database

Each layer has **one responsibility only.**

---

## 4. The Core Confusion: “Why So many Repositories?”

A major question during migration was:

> “We already have a repository port — why do we still need a JPA repository?”

### The Answer (Critical Insigth)

Because **they serve completely different purposes**.

---

## 5. Roles Explained (Very Important)

### 5.1 Domain Repository Port

```path
domain/port/repository/SubjectRepositoryPort
```

**What it is**

- A **business contract**
- Describes *what* persistance operations the doamin needs

Examples:

- save subject
- find active Subjects
- find Subject by ID

**What it is NOT**

- Not executable
- Not connected to the database
- Not something Spring can implement

It exits to keep **business logic independent of technology**.

---

### 5.2 Spring Data JPA Repository

```path
adapter/persistence/jpa/repository/SubjectJpaRepository
```

**What it is**

- A Spring Data interface
- Extends `JpaRepository`
- Talks directly to the database

**Why it is required**

- Spring only generates database code for `JpaRepository`
- Without it, no database access is possible

This file **cannot be removed**

---

### 5.3 Repository Adapter(The Bridge)

```path
adapter/persistence/jpa/repository/SubjectJpaRepositoryAdapter
```

**What it does**

- Implements the **domain repository port**
- Uses the **Spring Data JPA repository internally**
- Converts:
  - Domain model ↔ JPA entity
  - `Instant` ↔ `LocalDataTime`

The adapter is the **only place** where domain and persistence meet.


---

## 6. Why Removing `SubjectJpaRepository` Causes Errors

If `SubjectJpaRepository` is removed:

- Java compilation fails (type not found)
- Spring cannot create a database bean
- Adapter cannot be constructured
- Application fails to start

This is expected and correct behavior.

> A **port defines requirements** — it does not execute them.

---

## 7. Key Issues That happened During Migration

### 7.1 Multiple “Subject” Classes

There were multiple `Subject` representations:

- Domain model (`domain.model.Subject`)
- JPA entity (`adapter.persistence.jpa.entity.Subject`)
- External / legacy reference (e.g, Sakai)

**Issue**: They were accidentally treated as the same thing.

**Fix**: Clear separation + explicit mapping.

---

### 7.2 Repository Naming Collision

At one point, both of these existed:

- `SubjectRepository` (domain port)
- `SubjectRepository` (Spring expectation)

This caused confusion and wiring issues.

**Fixes**:

- Domain port renamed to `SubjectRepositoryPort`
- JPA repository kept as `SubjectJpaRepository`

---

### 7.3 Time Type Mismatch

- Domain used `Instant`
- Entity used `LocalDataTime`

**Fix**:

- Domain kept `Instant`
- Entity kept `LocalDataTime`
- Adapter performs conversion

This is the correct Clean Architecture approach.

---

## 8. Final Clean Architecture Structure (Subject)

```Structure
domain
├── model
│ └── Subject
└── port
└── repository
└── SubjectRepositoryPort

application
└── usecase
├── CreateSubjectUseCase
├── GetAllSubjectsUseCase
├── UpdateSubjectUseCase
└── DeleteSubjectUseCase

adapter
├── controller
│ └── SubjectController
└── persistence
└── jpa
├── entity
│ └── Subject
└── repository
├── SubjectJpaRepository
└── SubjectJpaRepositoryAdapter
```

---

## 9. End-to-End Subject Flow (How It Works)

### Example: Get All Subjects

Client
↓
SubjectController
↓
GetAllSubjectsUseCase
↓
SubjectRepositoryPort (domain contract)
↓
SubjectJpaRepositoryAdapter
↓
SubjectJpaRepository (Spring Data)
↓
Database

---

## 10. Responsibility by layer

### Controller

- Handles HTTP only
- No business logic
- Calls use cases

### Use Case

- Orchestrates application flow
- Uses domain models
- Depends only on ports

### Domain

- Core Business Logic
- Pure Java
- No Spring / JPA

### Repository Port

- Defines persistence needs
- Keeps domain independent

### Adapter

- Convers domain ↔ persistence
- Implements the port

### JPA Repository

- Executes real database queries
- Fully managed by Spring

---

## 11. Why This Architecture Is Valuable

### Technical Benefits

- Strong separation of concerns
- Compile-time enforcement of boundaries
- Easier testing
- Safer refactoring
- Framework independence

### Business Benefits

- Lower long-term maintenance cost
- Faster feature development
- Reduced regression risk
- Easier onboarding of developers

---

## 12. Final One-Line Explanation for management

> “We separated business logic from database and framework code so the Subject module remains stable, testable, and scalable even if technology changes.”

---

## 13. Key Takeaway

> **Ports describe what the system needs.
> Repositories do the actual work.
> Adapters connect the two.**

All three are required - removing any one breaks the system by design.

---

## 14. Conclusion

The Subject module is now:

- Correctly aligned with Clean Architecture
- Protected from framework coupling
- Easier to maintain and extend
- Architecturally sound for future growth
