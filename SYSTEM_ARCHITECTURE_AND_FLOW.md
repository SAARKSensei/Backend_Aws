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
          "refreshToken": "eyJhbGci..."
      }
  }
  ```
* **Frontend Action:** Save BOTH tokens in secure storage. Attach `accessToken` to all subsequent API calls in the header: `Authorization: Bearer <accessToken>`.

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

### 2A. Update Parent Profile
* **Endpoint:** `PUT /api/parent-users/{parentId}`
* **Request (JSON):**
  ```json
  {
      "name": "John Doe",
      "email": "john@example.com",
      "phone": "9876543210",
      "location": "Mumbai"
  }
  ```
* **Response:** Returns updated `ParentUserDTO`.

### 2B. Fetch Parent Transactions
Parents can view their history of wallet top-ups and plan purchases.
* **Endpoint:** `GET /api/parent-users/{parentId}/transactions`
* **Response:** Returns a list of `MasterTransaction` objects detailing amounts and dates.

### 2C. Parent Quiz (Optional baseline test)
* **Fetch Questions:** `GET /api/v1/parent-quiz`
* **Submit Answers:** `POST /api/v1/parent-quiz/submit`
  ```json
  {
      "answers": [
          {"questionId": "uuid-1", "selectedOptionId": "opt-uuid"}
      ]
  }
  ```

---

## 3. Child Registration

Parents must register their children to start learning. 

### 3A. Create a Child
* **Endpoint:** `POST /api/children`
* **Request (JSON):**
  ```json
  {
      "parentId": "<parent_uuid>",
      "childName": "Jane Doe",
      "gender": "Female",
      "grade": "5th",
      "ageGroup": "10-12",
      "bloodGroup": "O+",
      "schoolName": "Delhi Public School"
  }
  ```
* **Response:** Returns the created `ChildUserDTO` with a new `childId`.
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
* **Frontend Action:** Call this immediately after the Razorpay success callback to actually credit the wallet.

### 4B. Buy Pricing Plan
* **Create Order Endpoint:** `POST /api/payments/razorpay/order/plan`
* **Request (Form Data / URL Params):**
  ```text
  amount=1000
  parentId=<uuid>
  childId=<uuid>
  pricingPlanId=<uuid>
  ```
* **Response:** Returns `orderId`.

* **Verify Endpoint:** `POST /api/payments/razorpay/verify/plan`
* **Request (Form Data / URL Params):**
  ```text
  orderId=order_P123456
  paymentId=pay_P987654
  signature=e2b...
  ```
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
* **Response:** Array of `Subject` objects (access control enforced based on purchased plan).

### 5B. Get Modules & SubModules
* **Modules:** `GET /api/modules/by-subject/{subjectId}`
* **SubModules:** `GET /api/sub-modules/by-module/{moduleId}`

### 5C. Get Activities
* **Interactive:** `GET /api/interactive-activities/by-submodule/{subModuleId}`
* **Digital:** `GET /api/digital-activities/submodule/{subModuleId}`

---

## 6. Learning Progress Execution

> ⚠️ **CAUTION:** Frontend engineers MUST ensure these APIs are called sequentially to maintain data integrity for the child's Report Card.
### 6A. Start Activity
* **Endpoint:** `POST /api/progress/digital/start`
* **Request (JSON):**
  ```json
  {
      "childId": "uuid",
      "digitalActivityId": "uuid"
  }
  ```

### 6B. Attempt Questions
During a Digital Activity, questions are fetched via `GET /api/questions/digital-activity/{digitalActivityId}`. As the child answers:
* **Endpoint:** `POST /api/progress/question/attempt`
* **Request (JSON):**
  ```json
  {
      "childId": "uuid",
      "questionId": "uuid",
      "selectedOptionId": "uuid",
      "timeTakenSeconds": 15
  }
  ```

### 6C. Complete Activity
* **Endpoint:** `POST /api/progress/digital/complete`
* **Request (JSON):**
  ```json
  {
      "childId": "uuid",
      "digitalActivityId": "uuid"
  }
  ```

---

## 7. Report Cards

Parents can view the aggregated performance data of their children.

### 7A. Fetch Master Report Card
* **Endpoint:** `GET /api/v1/report-card/child/{childId}`
* **Response:** Returns aggregated metrics (score percentage, time spent, activity completion rates).

### 7B. Fetch Life Skills Mapping
* **Endpoint:** `GET /api/v1/child/{childId}/lifeskills`
* **Response:** Returns a computed breakdown of soft/hard skills developed based on completed interactive activities.
