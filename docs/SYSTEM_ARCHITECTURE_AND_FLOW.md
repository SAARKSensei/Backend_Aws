# Sensei App - Detailed API & Architecture Flow

This document serves as the **Technical Implementation Guide** for both Frontend and Backend Engineers. It maps out the exact sequence of API calls, distinguishing between Web and App clients, and providing payload/response structures for every step of the user journey.

> 💡 **IMPORTANT: Standard Response Format**
> Unless otherwise specified (like the Web Google Login), all REST endpoints return JSON wrapped in a standard `ApiResponse` format:
> ```json
> {
>     "status": "SUCCESS", // or "ERROR"
>     "message": "Descriptive message",
>     "data": { ... }, // Payload goes here
>     "timestamp": "2026-09-23T10:00:00"
> }
> ```

---

## 📑 Index
1. [Authentication Flow (Web vs App)](#1-authentication-flow-web-vs-app)
2. [Parent Onboarding & Profile](#2-parent-onboarding--profile)
3. [Child Registration](#3-child-registration)
4. [Wallet & Plan Purchases](#4-wallet--plan-purchases)
5. [Content Fetching Hierarchy](#5-content-fetching-hierarchy)
6. [Learning Progress Execution](#6-learning-progress-execution)
7. [Report Cards](#7-report-cards)

---

## 1. Authentication Flow (Web vs App)

*(For a deep-dive on how the stateless token rotation and interception specifically works, please see [AUTHENTICATION_FLOW.md](./AUTHENTICATION_FLOW.md))*

### 1A. Mobile App Login (Flutter)
The app needs both an Access Token and a Refresh Token to maintain a stateless session without forcing the user to log in again.

* **Endpoint:** `POST /api/auth/google?client=app`
* **Request (Form Data):** `idToken=<Google_JWT_Token>`
* **Response (JSON):**
  ```json
  {
      "status": "SUCCESS",
      "data": {
          "accessToken": "eyJhbGci...",
          "refreshToken": "eyJhbGci...",
          "isQuizCompleted": false
      }
  }
  ```
* **Frontend Action:** 
  1. Save BOTH tokens in secure storage. Attach `accessToken` to all subsequent API calls in the header: `Authorization: Bearer <accessToken>`.
  2. Read the `isQuizCompleted` flag. If `false`, route the user to the initial onboarding quiz. If `true`, route them directly to the main Dashboard.

### 1B. Web Login (React/Angular)
The web client currently relies on a standard access token string for backward compatibility.

* **Endpoint:** `POST /api/auth/google`
* **Request (Form Data):** `idToken=<Google_JWT_Token>`
* **Response (Plain String):**
  ```text
  eyJhbGciOiJIUzI1NiJ9.eyJzdWIi...
  ```
* **Frontend Action:** Save the string in `localStorage` or memory, attach it to `Authorization: Bearer <token>`.

### 1C. Token Refresh (App Only)
When the `accessToken` expires (Backend returns `401 Unauthorized`), the App must silently refresh.

* **Endpoint:** `POST /api/auth/refresh`
* **Request (JSON):**
  ```json
  { "refreshToken": "saved_refresh_token_here" }
  ```
* **Response (JSON):** Returns a new `accessToken` and `refreshToken`.

---

## 2. Parent Onboarding & Profile

After login, the frontend should fetch or create the Parent profile.

### 2A. Create Parent User (With Auto Child Creation)
* **Endpoint:** `POST /api/parent-users`
* **Request (JSON):**
  ```json
  {
      "name": "John Doe",
      "email": "john@example.com",
      "phone": "9876543210",
      "location": "Mumbai"
  }
  ```
* **Response (JSON):** Returns the created `ParentUserDTO`. Crucially, **the backend automatically generates an empty ChildUser** and attaches it to this response. Null values are excluded from the JSON to keep it clean.
  ```json
  {
      "parentId": "<parent_uuid>",
      "name": "John Doe",
      ...
      "childUsers": [
          {
              "childId": "<auto_generated_child_uuid>"
          }
      ]
  }
  ```
* **Frontend Action:** Extract `parentId` and the auto-generated `childId` (`childUsers[0].childId`) and store them in state to proceed to the quiz screen.

### 2B. Parent Quiz & Unlocking Baseline Life Skills
Immediately following parent creation, parents take a baseline quiz that assigns initial life skills to the newly generated child.
* **Fetch Questions:** `GET /api/v1/parent-quiz`
* **Submit Answers:** `POST /api/v1/parent-quiz/submit`
  * **Request (JSON):** 
    ```json
    {
        "parentId": "<parent_uuid>",
        "childId": "<child_uuid>",
        "selectedOptionIds": ["<opt_uuid_1>", "<opt_uuid_2>"]
    }
    ```
  * **Backend Processing:** Checks the associated life skills for the selected options and automatically unlocks them for the `childId` provided. Sets the parent's `isQuizCompleted` flag to true.
* **Fetch Attempt:** `GET /api/v1/parent-quiz/attempt/{parentId}/{childId}` (Retrieves previous options to pre-fill UI)
  * **Response (JSON):**
    ```json
    {
        "parentId": "<parent_uuid>",
        "childId": "<child_uuid>",
        "selectedOptionIds": ["<opt_uuid_1>", "<opt_uuid_2>"]
    }
    ```
* **Update Answers:** `PUT /api/v1/parent-quiz/update` (Dynamically supports partial and full updates by calculating the difference, reverting removed life skills, and adding new ones).
  * **Request (JSON):**
    ```json
    {
        "parentId": "<parent_uuid>",
        "childId": "<child_uuid>",
        "selectedOptionIds": ["<new_opt_uuid_1>"] 
    }
    ```
### 2C. Child Life Skills Dashboard
After the quiz, the frontend can fetch the initially unlocked life skills for the dashboard.
* **Endpoint:** `GET /api/v1/child/{childId}/lifeskills`
* **Response:** Returns a `ChildLifeSkillReportResponse` listing all skills the child currently possesses.

### 2D. Update Parent Profile
* **Endpoint:** `PUT /api/parent-users/{parentId}`

### 2E. Fetch Parent Transactions
Parents can view their history of wallet top-ups and plan purchases.
* **Endpoint:** `GET /api/parent-users/{parentId}/transactions`
* **Response:** Returns a list of `MasterTransaction` objects detailing amounts and dates.

### 2F. Account Deletion (Soft Delete)
When a parent requests account deletion, the system performs a **Soft Delete** to preserve data integrity and prevent foreign key crashes, while physically revoking their login access.
* **Endpoint:** `DELETE /api/parent-users/{parentId}`
* **Backend Processing:**
  1. The user's account is permanently deleted from **Firebase Auth**.
  2. The database updates the `ParentUser` and all associated `ChildUser` profiles by setting `is_deleted = true` and `deleted_at = NOW()`.
  3. The user's `email` and `user_name` are prefixed with `del-` and suffixed with a timestamp to free up those identifiers for future re-registration and avoid `UNIQUE` constraint errors.
  4. Global `@SQLRestriction` automatically hides these users from all standard application APIs.
* **Admin Fetch (Graveyard API):** Because of the `@SQLRestriction`, deleted users are hidden. To view them, admins must use the native query projection endpoint (e.g., `GET /api/parent-users/admin/deleted`).

---

## 3. Child Registration & Management

While an empty child is automatically created upon parent registration, parents can fully update the child's profile or add additional children later.

### 3A. Update Child Profile
Since the initial empty child is auto-created during parent registration, you only need to use the PUT endpoint to fill in the child's details.
* **Endpoint:** `PUT /api/children/{childId}`
* **Request (JSON):**
  ```json
  {
      "childName": "Jane Doe",
      "gender": "Female",
      "grade": "5th",
      "ageGroup": "10-12",
      "bloodGroup": "O+",
      "schoolName": "Delhi Public School"
  }
  ```
* **Response:** Returns the updated/created `ChildUserDTO`.
* **Frontend Action:** Store the active `childId` in state to use for subsequent content fetching.

### 3B. Fetch Children for a Parent
* **Endpoint:** `GET /api/children/parent/{parentId}`
* **Response:** Returns an array of `ChildUserDTO` objects. 
* **Note on Plan Expiration Fields:** The returned child object contains crucial fields for access control:
  - `activePlanId`: The UUID of their current plan.
  - `planStartDate`: When the plan was activated.
  - `planExpiryDate`: When the plan expires.
  - `planStatus`: e.g., `ACTIVE` or `EXPIRED`. If expired, the frontend should prompt the parent to renew before allowing access to locked subjects.

---

## 4. Wallet & Plan Purchases

To access subjects, a child must have an active plan.

### 4A. Wallet Top-Up
* **Create Order Endpoint:** `POST /api/payments/razorpay/order/wallet`
* **Request (Form Data / URL Params):**
  ```text
  amount=1000
  parentId=<uuid>
  ```
* **Response:** Returns Razorpay `orderId` to initialize the frontend payment gateway SDK.

* **Verify Endpoint:** `POST /api/payments/razorpay/verify/wallet`
* **Request (Form Data / URL Params):**
  ```text
  orderId=order_P123456
  paymentId=pay_P987654
  signature=e2b...
  ```
* **Where do these come from?** The `orderId` comes from step 1. The `paymentId` and `signature` are generated by Razorpay and passed to the frontend via the Razorpay SDK's `handler` callback immediately after the user successfully completes the payment in the UI popup.
* **Frontend Action:** Call this immediately after the Razorpay success callback to actually credit the wallet.

### 4B. Buy Pricing Plan
* **Initiate Purchase Endpoint:** `POST /api/plan-purchases`
* **Request (JSON):**
  ```json
  {
      "parentId": "<uuid>",
      "childId": "<uuid>",
      "pricingPlanId": "<uuid>",
      "couponCode": "SUMMER50",    // (Optional)
      "walletAmountUsed": 100      // (Optional)
  }
  ```
* **Response:**
  * **If fully paid by wallet/coupon:** Returns `{ "status": "SUCCESS", "message": "PLAN_ACTIVATED_SUCCESSFULLY" }` (Plan is instantly activated, skip verification step).
  * **If payment is required:** Returns `{ "status": "PAYMENT_REQUIRED", "orderId": "order_XYZ123", "amount": 50000, "currency": "INR" }`. Use this `orderId` to initialize the frontend Razorpay payment gateway.

* **Verify Endpoint:** `POST /api/payments/razorpay/verify/plan`
* **Request (Form Data / URL Params):**
  ```text
  orderId=order_P123456
  paymentId=pay_P987654
  signature=e2b...
  ```
* **Where do these come from?** Similar to wallet top-up, these three fields are provided by the Razorpay frontend SDK inside the success `handler` callback.
* **Frontend Action:** Call this immediately after the Razorpay success callback to activate the plan for the child.

### 4C. How Plan Activation & Expiration Works (Backend Mechanics)
When the frontend calls the plan purchase API, the backend resolves the payment through a waterfall calculation:
1. **Coupons:** If a valid `couponCode` is provided, the backend applies the discount to the base price.
2. **Wallet:** If `walletAmountUsed` is provided, it deducts that amount from the parent's Wallet (logging a transaction).
3. **Razorpay:** If there's still a remaining balance (Payable Amount > 0), it generates a Razorpay Order ID for the frontend to complete the payment.
4. **Activation:** Once fully paid (or if free), the backend updates the `ChildUserDTO`. It sets the `planStartDate` to today, calculates the `planExpiryDate` based on the plan's duration (e.g., +365 days), and sets `planStatus` to `ACTIVE`. 
5. **Expiration Enforcement:** The backend enforces these dates. If a child tries to fetch `Subject` data after the `planExpiryDate`, the API will deny access until a new purchase is made.

---

## 5. Content Fetching Hierarchy

The educational content in Sensei is strictly hierarchical. The frontend builds the learning UI by navigating down this data model tree.

### 🌳 Data Model Hierarchy

```text
[Pricing Plan] (Grants Access)
      │
      └── [Subject]
             │
             └── [Module]
                    │
                    └── [SubModule]
                           │
                           ├── [Digital Activity]
                           │      └── [Question]
                           │             └── [Question Options]
                           │
                           └── [Interactive Activity]
                                  └── [Interactive Process]
                                         └── [Interactive Process SubStep]
```

**Hierarchy Breakdown:**
- **Subject** (`/api/subjects`): Broad topics
  - ↳ **Module** (`/api/modules`): Broad units
    - ↳ **SubModule** (`/api/sub-modules`): Specific topics
      - ↳ **Activities**: The actual learning tasks.
        - **Interactive Activity** (`/api/interactive-activities`): Physical tasks guided by `InteractiveProcess`.
        - **Digital Activity** (`/api/digital-activities`): On-screen tasks containing multiple `Questions`.

### 5A. Get Subjects for Child
* **Endpoint:** `GET /api/subjects?childId={childId}`
* **Response:** Array of `Subject` objects (access control enforced based on purchased plan, **plus any globally configured Freemium Subjects**). If `?childId=` is provided, each Subject will include a `progress` object (see Section 6D for schema) and an `isLocked` boolean.

### 5B. Get Modules & SubModules
* **Modules:** `GET /api/modules/by-subject/{subjectId}?childId={uuid}`
* **SubModules:** `GET /api/sub-modules/by-module/{moduleId}?childId={uuid}`

**Example Response Injection (If wrapped in standard ApiResponse):**
```json
{
  "status": "SUCCESS",
  "message": "Fetched successfully",
  "data": [
    {
      "id": "uuid",
      "name": "Exploring Feelings",
      "description": "Learn to identify emotions.",
      "orderIndex": 1,
      "isActive": true,
      "isLocked": false, // <-- NEW: Indicates if the frontend should draw a padlock!
      "progress": {
        "completedCount": 2,
        "totalCount": 4,
        "isCompleted": false,
        "status": "STARTED"
      }
    }
  ],
  "timestamp": "2026-10-01T10:00:00"
}
```
**What the injected `progress` fields mean:**
* `completedCount`: The number of nested items the child has finished. For a Subject, this is completed Modules. For a Module, this is completed SubModules. For a SubModule, this is completed Activities.
* `totalCount`: The total number of active nested items available inside this parent.
* `isCompleted`: A strict boolean (`true` or `false`). It only becomes `true` when `completedCount >= totalCount` (i.e., the child has 100% finished this entire section). The frontend can use this to display a "100% Mastered!" badge.
* `status`: Automatically computed as `"COMPLETED"`, `"STARTED"`, or `"NOT_STARTED"`.
* `isLocked`: Determines if the child has access to this content. If `true`, the frontend should display a padlock, and any API requests to fetch its children will return a `403 Forbidden` error.

### 5C. Get Activities
* **Interactive:** `GET /api/interactive-activities/by-submodule/{subModuleId}?childId={uuid}`
* **Digital:** `GET /api/digital-activities/submodule/{subModuleId}?childId={uuid}`

**Example Response Injection (If wrapped in standard ApiResponse):**
```json
{
  "status": "SUCCESS",
  "message": "Fetched successfully",
  "data": [
    {
      "id": "uuid",
      "subModuleId": "parent-submodule-uuid",
      "title": "Drag the Colors",
      "gameType": "DRAG_AND_DROP",
      "difficulty": "EASY",
      "orderIndex": 1,
      "isActive": true,
      "status": "STARTED" // <-- THIS IS INJECTED AUTOMATICALLY!
    }
  ],
  "timestamp": "2026-10-01T10:00:00"
}
```
*Note: The `status` field is only injected if the child has started or completed the activity and the `?childId=uuid` parameter is provided. Otherwise, it will be omitted or null.*

**What the injected `status` means:**
* `"STARTED"`: The child tapped the activity and began playing, but never formally finished it. The frontend should display a **"Resume"** or **"Jump Back In!"** badge.
* `"COMPLETED"`: The child successfully finished the activity. The frontend should display a **"Completed"** badge (e.g., a green checkmark).

---

## 6. Learning Progress Execution & Tracking

> ⚠️ **CAUTION:** Frontend engineers MUST use the bulk-submission approach for Digital Activities to ensure offline resilience and robust AI micro-tracking.

### 6A. Start Activity
**When to call:** The frontend MUST trigger this endpoint the exact moment the child taps to open/start an activity. This registers the initial entry in the database and ensures we accurately track that the child has engaged with the content.

* **Digital:** `POST /api/progress/digital/start`
* **Interactive:** `POST /api/progress/activity/start`
* **Request (JSON):**
  ```json
  {
      "childId": "uuid",
      "digitalActivityId": "uuid" // or interactiveActivityId
  }
  ```

### 6B. Complete Digital Activity (Bulk Micro-Tracking)
Instead of hitting the server for every single question click, the frontend should maintain a local timer and log every attempt (including wrong answers and hesitations). Submit it all at once when the child hits "Finish".
* **Endpoint:** `POST /api/progress/digital/complete`
* **Request (JSON):**
  ```json
  {
      "childId": "uuid",
      "digitalActivityId": "uuid",
      "timeTakenSeconds": 120,          
      "feedbackStars": 5,               
      "feedbackMessage": "I loved it!", 
      "attempts": [                     
          {
              "questionId": "Q1_uuid",
              "optionId": "Wrong_Option_A_uuid",
              "timeTakenSeconds": 15
          },
          {
              "questionId": "Q1_uuid",
              "optionId": "Right_Option_uuid",
              "timeTakenSeconds": 5
          }
      ]
  }
  ```
* **Backend Processing:** The backend will automatically unpack the `attempts` array, increment attempt numbers, and record the distractor hesitation times. It will then instantly recalculate and cache the completed fractional progress for the SubModule, Module, and Subject levels!

### 6C. Complete Interactive Activity
* **Endpoint:** `POST /api/progress/activity/complete`
* **Request (JSON):**
  ```json
  {
      "childId": "uuid",
      "interactiveActivityId": "uuid",
      "timeTakenSeconds": 300,
      "feedbackStars": 4,
      "feedbackMessage": "Fun but a bit hard."
  }
  ```

### 6D. Displaying Cached Progress (Frontend Integration)
Because the backend instantly calculates the progress hierarchy upon activity completion, you don't need to call a separate tracking API! 
Just pass `?childId=uuid` to the standard content endpoints.

**For Modules & SubModules:**
The API will inject a `progress` object:
* `GET /api/modules/by-subject/{subjectId}?childId={uuid}`
* `GET /api/sub-modules/by-module/{moduleId}?childId={uuid}`

```json
{
  "status": "SUCCESS",
  "message": "Fetched successfully",
  "data": [
    {
      "id": "uuid",
      "moduleId": "parent-module-uuid",
      "name": "Exploring Feelings",
      "description": "Learn to identify emotions.",
      "orderIndex": 1,
      "isActive": true,
      "isLocked": false,
      "progress": {
        "completedCount": 2,
        "totalCount": 4,
        "isCompleted": false,
        "status": "STARTED"
      }
    }
  ],
  "timestamp": "2026-10-01T10:00:00"
}
```
**What the injected `progress` fields mean:**
* `completedCount`: The number of nested items the child has finished. For a Subject, this is completed Modules. For a Module, this is completed SubModules. For a SubModule, this is completed Activities.
* `totalCount`: The total number of active nested items available inside this parent.
* `isCompleted`: A strict boolean (`true` or `false`). It only becomes `true` when `completedCount >= totalCount`. The frontend can use this to display a "100% Mastered!" badge.
* `status`: Automatically computed as `"COMPLETED"` (if 100% finished), `"STARTED"` (if completedCount > 0), or `"NOT_STARTED"`. Used to show badges like "In Progress".

**For Activities (Both Interactive and Digital):**
The API will inject a `status` string into BOTH Interactive and Digital Activity objects:
* `GET /api/digital-activities/submodule/{subModuleId}?childId={uuid}`
* `GET /api/interactive-activities/by-submodule/{subModuleId}?childId={uuid}`

**Example Digital Activity Response:**
```json
{
  "status": "SUCCESS",
  "message": "Fetched successfully",
  "data": [
    {
      "id": "uuid",
      "subModuleId": "parent-submodule-uuid",
      "title": "Drag the Colors",
      "gameType": "DRAG_AND_DROP",
      "difficulty": "EASY",
      "orderIndex": 1,
      "isActive": true,
      "status": "STARTED" 
    }
  ],
  "timestamp": "2026-10-01T10:00:00"
}
```
**What the injected `status` means:**
* `"STARTED"`: The child tapped the activity and began playing, but never formally finished it. The frontend should display a **"Resume"** or **"Jump Back In!"** badge.
* `"COMPLETED"`: The child successfully finished the activity. The frontend should display a **"Completed"** badge (e.g., a green checkmark).
---

## 7. Report Cards

Parents can view the aggregated performance data of their children.

### 7A. Fetch Master Report Card
* **Endpoint:** `GET /api/v1/report-card/child/{childId}`
* **Response:** Returns aggregated metrics (score percentage, time spent, activity completion rates).

### 7B. Fetch Life Skills Mapping
* **Endpoint:** `GET /api/v1/child/{childId}/lifeskills`
* **Response:** Returns a computed breakdown of soft/hard skills developed based on completed interactive activities.
