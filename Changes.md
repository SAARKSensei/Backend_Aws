# Changes

```a
with clean Architecture
com.sensei.backend
├── domain                     ← NO Spring annotations
│   ├── model                  ← Core business objects
│   ├── valueobject
│   ├── service                ← Domain services (rules, logic)
│   └── port
│       └── repository         ← Interfaces only
│
├── application
│   ├── usecase                ← Orchestration logic
│   └── dto
│
├── adapter
│   ├── controller             ← REST controllers
│   └── persistence
│       ├── jpa
│       │   ├── entity
│       │   └── repository     ← JPA impl of domain ports
│
├── infrastructure
│   ├── config
│   ├── security
│   └── external               ← AI, payment, email, etc
│
├── exception
└── util"
```

```b
Current
com.sensei.backend
├── controller
├── service
├── repository
├── entity
├── dto
├── exception / error
├── config
├── util
```

1. Creating a centralized logging system.
