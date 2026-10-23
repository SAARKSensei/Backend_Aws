# 🗄️ Database Entities & Frontend API Parameters

This guide maps **what is stored in the database** against the **exact JSON parameter names the frontend should send/receive in API calls** for **ParentUser** and **ChildUser**.

---

## 📑 Quick Navigation
1. [Parent User (Entity vs Frontend API)](#1-parent-user-entity-vs-frontend-api)
2. [Child User (Entity vs Frontend API)](#2-child-user-entity-vs-frontend-api)
3. [Important Field Mappings & Gotchas](#3-important-field-mappings--gotchas)

---

## 1. Parent User (Entity vs Frontend API)

- **Database Table:** `parent_user`
- **Primary API Endpoints:** 
  - Create: `POST /api/parent-users`
  - Get: `GET /api/parent-users/{parentId}`
  - Update: `PUT` / `PATCH /api/parent-users/{parentId}`

### Parameter Mapping Table

| Frontend API Parameter (JSON Key) | Database Column | Data Type | Request | Response | Description & Example |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `parentId` | `parent_id` | `UUID` | Auto / Path param | ✅ Yes | Unique identifier (e.g. `"3fa85f64-5717-4562-b3fc-2c963f66afa6"`) |
| `name` | `name` | `VARCHAR(255)` | Required (`@NotBlank`) | ✅ Yes | Parent's full name (e.g. `"John Doe"`) |
| `userName` | `user_name` | `VARCHAR(255)` | Optional (Unique) | ✅ Yes | Unique username handle (e.g. `"johndoe_99"`) |
| `email` | `email` | `VARCHAR(255)` | Required (Unique, `@Email`) | ✅ Yes | Parent's email (e.g. `"john@example.com"`) |
| `phone` | `phone` | `VARCHAR(255)` | Required (10 digits) | ✅ Yes | 10-digit mobile number (e.g. `"9876543210"`) |
| `childLock` | `child_lock` | `VARCHAR(255)` (BCrypt) | Required on create (4 digits) | ❌ Never (Write-only) | 4-digit numeric PIN for parental control (e.g. `"1234"`). Stored encrypted in DB. |
| `isChildLockSet` | *(Computed)* | `BOOLEAN` | ❌ Do not send | ✅ Yes | Returns `true` if a child lock PIN exists, `false` otherwise. |
| `maritalStatus` | `marital_status` | `VARCHAR(255)` | Optional | ✅ Yes | Marital status (e.g. `"Married"`, `"Single"`) |
| `occupation` | `occupation` | `VARCHAR(255)` | Optional | ✅ Yes | Parent's occupation (e.g. `"Software Engineer"`) |
| `dateOfBirth` | `date_of_birth` | `DATE` | Optional | ✅ Yes | Date of birth in ISO format (e.g. `"1985-06-15"`) |
| `relationWithChildren` | `relation_with_children` | `VARCHAR(255)` | Optional | ✅ Yes | Primary relationship (e.g. `"Father"`, `"Mother"`) |
| `spouseName` | `spouse_name` | `VARCHAR(255)` | Optional | ✅ Yes | Full name of spouse (e.g. `"Jane Doe"`) |
| `spouseGender` | `spouse_gender` | `VARCHAR(255)` | Optional | ✅ Yes | Gender of spouse (e.g. `"Female"`) |
| `spouseEmail` | `spouse_email` | `VARCHAR(255)` | Optional | ✅ Yes | Spouse's email address |
| `spousePhone` | `spouse_phone` | `VARCHAR(255)` | Optional | ✅ Yes | Spouse's phone number |
| `spouseOccupation` | `spouse_occupation` | `VARCHAR(255)` | Optional | ✅ Yes | Spouse's profession |
| `spouseDateOfBirth` | `spouse_date_of_birth` | `DATE` | Optional | ✅ Yes | Spouse's birth date (e.g. `"1987-08-20"`) |
| `spouseRelationWithChild`| `spouse_relation_with_child`| `VARCHAR(255)` | Optional | ✅ Yes | Spouse relation (e.g. `"Mother"`, `"Guardian"`) |
| `street` | `street` | `VARCHAR(255)` | Optional | ✅ Yes | Street address (e.g. `"123 MG Road"`) |
| `city` | `city` | `VARCHAR(255)` | Optional | ✅ Yes | City name (e.g. `"Bengaluru"`) |
| `state` | `state` | `VARCHAR(255)` | Optional | ✅ Yes | State / Province (e.g. `"Karnataka"`) |
| `country` | `country` | `VARCHAR(255)` | Optional | ✅ Yes | Country (e.g. `"India"`) |
| `pincode` | `pincode` | `VARCHAR(255)` | Optional | ✅ Yes | Postal / ZIP code (e.g. `"560001"`) |
| `isQuizCompleted` | `is_quiz_completed` | `BOOLEAN` | Optional (Update) | ✅ Yes | Indicates if onboarding quiz is completed (`true`/`false`) |
| `childUsers` | *(Relationship)* | `List<ChildUserDTO>` | ❌ Do not send on create | ✅ Yes | List of associated children objects returned in GET |

#### Internal Database Audit Columns (Never sent by frontend):
- `created_at`: Timestamp when record was created (`timestamp(6)`)
- `updated_at`: Timestamp when record was last updated (`timestamp(6)`)
- `is_deleted`: Soft-delete status flag (`boolean`, default `false`)
- `deleted_at`: Timestamp of soft-deletion (`timestamp`)

---

## 2. Child User (Entity vs Frontend API)

- **Database Table:** `child_user`
- **Primary API Endpoints:**
  - Create: `POST /api/children`
  - Get: `GET /api/children/{childId}`
  - Update: `PUT` / `PATCH /api/children/{childId}`
  - Get by Parent: `GET /api/children/parent/{parentId}`

### Parameter Mapping Table

| Frontend API Parameter (JSON Key) | Database Column | Data Type | Request | Response | Description & Example |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `childId` | `child_id` | `UUID` | Auto / Path param | ✅ Yes | Unique identifier (e.g. `"7ab12c34-89de-4123-bf34-abcdef123456"`) |
| `parentId` | `parent_id` | `UUID` | Required (`@NotNull`) | ✅ Yes | ID of the parent account this child belongs to |
| `childName` | `child_name` | `VARCHAR(255)` | Required (`@NotBlank`) | ✅ Yes | Child's full name (e.g. `"Aarav Sharma"`) |
| `gender` | `gender` | `VARCHAR(255)` | Optional | ✅ Yes | Gender (e.g. `"Male"`, `"Female"`) |
| `grade` | `grade` | `VARCHAR(255)` | Required (`@NotBlank`) | ✅ Yes | School grade/class (e.g. `"Grade 3"`, `"Class 5"`) |
| `ageGroup` | `age_group` | `VARCHAR(255)` | Optional | ✅ Yes | Age bucket (e.g. `"6-8"`, `"8-10"`) |
| `bloodGroup` | `blood_group` | `VARCHAR(255)` | Optional | ✅ Yes | Blood group (e.g. `"B+"`, `"O+"`) |
| `phoneNumber` | `phone_number` | `VARCHAR(255)` | Optional | ✅ Yes | Child's phone (if applicable) |
| `dateOfBirth` | `date_of_birth` | `DATE` | Optional | ✅ Yes | Date of birth (e.g. `"2016-04-12"`) |
| `schoolName` ⚠️ | `school_id` | `VARCHAR(255)` | Optional | ✅ Yes | Name of the school (e.g. `"Delhi Public School"`). *Mapped to `school_id` column in DB.* |
| `schoolPhone` | `school_phone` | `VARCHAR(255)` | Optional | ✅ Yes | School contact phone number (e.g. `"020-26123456"`) |
| `schoolEmail` | `school_email` | `VARCHAR(255)` | Optional | ✅ Yes | School contact email (e.g. `"info@dps.edu"`) |
| `schoolAddress` | `school_address` | `VARCHAR(255)` | Optional | ✅ Yes | Full address of the school (e.g. `"MG Road, Camp, Pune"`) |
| `medicalHistory` | `medical_history` | `VARCHAR(255)` | Optional | ✅ Yes | Child medical notes / allergies (e.g. `"Asthma, peanut allergy"`) |
| `pfp` | `pfp` | `VARCHAR(255)` | Optional | ✅ Yes | Profile picture Name/URL or image key (e.g. `"https://cdn.example.com/pfp/child1.png"`) |
| `activePlanId` | `active_plan_id` | `UUID` | Optional | ✅ Yes | UUID of the currently subscribed pricing plan |
| `planStartDate` | `plan_start_date` | `DATE` | Optional (Auto-set) | ✅ Yes | Date when active plan started (e.g. `"2026-10-01"`) |
| `planExpiryDate` | `plan_expiry_date` | `DATE` | Optional | ✅ Yes | Date when active plan expires (e.g. `"2027-10-01"`) |
| `planStatus` | `plan_status` | `VARCHAR(255)` | Optional | ✅ Yes | Subscription status enum (`ACTIVE`, `EXPIRED`, `INACTIVE`) |

#### Internal Database Audit Columns (Never sent by frontend):
- `created_at`: Timestamp when record was created (`timestamp(6)`)
- `updated_at`: Timestamp when record was last updated (`timestamp(6)`)
- `is_deleted`: Soft-delete status flag (`boolean`, default `false`)
- `deleted_at`: Timestamp of soft-deletion (`timestamp`)

---

## 3. Important Field Mappings & Gotchas

### ⚠️ Gotcha 1: `schoolName` vs `school_id`
- **Frontend sends:** `"schoolName": "Greenwood High"`
- **Backend stores in DB column:** `school_id`
- **Why:** The entity has `@Column(name = "school_id") private String schoolName;`. Always use the key `schoolName` in the frontend API request and response JSON.

### ⚠️ Gotcha 2: `childLock` vs `isChildLockSet`
- **Creating/Updating:** Frontend sends `"childLock": "1234"` (4 digits).
- **Backend handling:** The backend hashes it with BCrypt before storing it in `child_lock`.
- **Fetching:** Backend **NEVER** sends the PIN back. Instead, frontend receives `"isChildLockSet": true`.

### ⚠️ Gotcha 3: Automatic Initial Child Creation
- When `POST /api/parent-users` is called, the backend automatically creates an empty initial `ChildUser` linked to the parent. The frontend can immediately fetch or update this child with `PUT /api/children/{childId}`.

### ⚠️ Gotcha 4: Address Columns
- Old columns `address` and `location` were dropped.
- Frontend must use the specific fields: `street`, `city`, `state`, `country`, and `pincode`.

---

## 4. Frontend Payload Examples

### A. Create Parent User (`POST /api/parent-users`)
```json
{
  "name": "Rajesh Kumar",
  "userName": "rajesh_k",
  "email": "rajesh.kumar@example.com",
  "phone": "9876543210",
  "childLock": "4321",
  "maritalStatus": "Married",
  "occupation": "Doctor",
  "dateOfBirth": "1984-11-23",
  "relationWithChildren": "Father",
  "spouseName": "Pooja Kumar",
  "spousePhone": "9876543211",
  "street": "12 Lotus Boulevard",
  "city": "Pune",
  "state": "Maharashtra",
  "country": "India",
  "pincode": "411001"
}
```

### B. Create Child User (`POST /api/children`)
```json
{
  "parentId": "c56a4180-65aa-42ec-a945-5fd21dec0538",
  "childName": "Ananya Kumar",
  "gender": "Female",
  "grade": "Grade 4",
  "ageGroup": "8-10",
  "bloodGroup": "B+",
  "schoolName": "The Bishop's School",
  "schoolPhone": "020-26123456",
  "schoolEmail": "contact@bishops.edu",
  "schoolAddress": "Camp, Pune, Maharashtra",
  "medicalHistory": "Mild asthma, allergic to penicillin",
  "pfp": "NAME",
  "dateOfBirth": "2016-05-14"
}
```

### C. Update Child Lock PIN (`PUT /api/parent-users/{parentId}/child-lock`)
```json
{
  "currentPin": "4321",
  "newPin": "9876"
}
```

### D. Verify Child Lock PIN (`POST /api/parent-users/{parentId}/child-lock/verify`)
```json
{
  "pin": "9876"
}
```
