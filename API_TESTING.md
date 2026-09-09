# API Testing Guide

Below is a list of all active API endpoints available in the system for testing. (Note: Commented out endpoints are omitted).

> [!IMPORTANT]
> **General Testing Notes:**
> - **Authentication:** Most of these endpoints (except `/api/auth/google` and `/api/auth/test-login`) are secured and require a valid JWT token. You must pass this in the header: `Authorization: Bearer <your_access_token>`.
> - **POST/PUT Requests:** Endpoints that create or update data (`POST`, `PUT`, `PATCH`) typically require a JSON request body (`Content-Type: application/json`) with the necessary object fields.

## InteractiveProcessSubStep

**Base Path:** `/api/interactive-process-substeps`

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/interactive-process-substeps` | `create` |
| `GET` | `/api/interactive-process-substeps/by-process/{processId}` | `getByProcess` |
| `GET` | `/api/interactive-process-substeps/{subStepId}` | `getById` |
| `PUT` | `/api/interactive-process-substeps/{subStepId}` | `update` |
| `DELETE` | `/api/interactive-process-substeps/{subStepId}` | `delete` |


## Coupon

**Base Path:** ``

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/admin/coupons` | `createCoupon` |
| `POST` | `/api/coupons/validate` | `validateCoupon` |
| `GET` | `/api/admin/coupons` | `listCoupons` |
| `GET` | `/api/admin/coupons/{id}` | `getCoupon` |
| `PUT` | `/api/admin/coupons` | `updateCoupon` |
| `PATCH` | `/api/admin/coupons/{id}/disable` | `disableCoupon` |
| `DELETE` | `/api/admin/coupons/{id}` | `deleteCoupon` |


## OTP

**Base Path:** `/api/v1/otp`

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/v1/otp/send` | `sendOTP` |
| `POST` | `/api/v1/otp/resend` | `resendOTP` |
| `POST` | `/api/v1/otp/verify` | `verifyOTP` |


## PricingPlanSubject

**Base Path:** `/api/pricing-plan-subjects`

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/pricing-plan-subjects/attach-multiple` | `attachMultiple` |
| `GET` | `/api/pricing-plan-subjects` | `getAll` |
| `GET` | `/api/pricing-plan-subjects/by-plan/{planId}` | `getByPlan` |
| `GET` | `/api/pricing-plan-subjects/by-subject/{subjectId}` | `getBySubject` |
| `PUT` | `/api/pricing-plan-subjects/{mappingId}` | `update` |
| `DELETE` | `/api/pricing-plan-subjects/{mappingId}` | `delete` |


## PlanPurchase

**Base Path:** `/api/plan-purchases`

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/plan-purchases` | `purchasePlan` |


## Module

**Base Path:** `/api/modules`

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/modules` | `create` |
| `GET` | `/api/modules/by-subject/{subjectId}` | `getBySubject` |
| `GET` | `/api/modules/{id}` | `getById` |
| `PUT` | `/api/modules/{id}` | `update` |
| `DELETE` | `/api/modules/{id}` | `delete` |
| `GET` | `/api/modules` | `getAll` |


## DigitalActivity

**Base Path:** `/api/digital-activities`

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/digital-activities` | `create` |
| `PUT` | `/api/digital-activities/{id}` | `update` |
| `DELETE` | `/api/digital-activities/{id}` | `delete` |
| `GET` | `/api/digital-activities/{id}` | `getById` |
| `GET` | `/api/digital-activities/submodule/{subModuleId}` | `getBySubModule` |


## Health

**Base Path:** ``

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/health` | `health` |


## Razorpay

**Base Path:** `/api/payments/razorpay`

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/payments/razorpay/order/plan` | `createPlanOrder` |
| `POST` | `/api/payments/razorpay/verify/plan` | `verifyPlanPayment` |
| `POST` | `/api/payments/razorpay/order/wallet` | `createWalletTopupOrder` |


## InteractiveProcess

**Base Path:** `/api/interactive-processes`

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/interactive-processes` | `create` |
| `GET` | `/api/interactive-processes/by-activity/{activityId}` | `getByActivity` |
| `GET` | `/api/interactive-processes/{processId}` | `getById` |
| `PUT` | `/api/interactive-processes/{processId}` | `update` |
| `DELETE` | `/api/interactive-processes/{processId}` | `delete` |


## Question

**Base Path:** `/api/questions`

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/questions` | `create` |
| `PUT` | `/api/questions/{id}` | `update` |
| `DELETE` | `/api/questions/{id}` | `delete` |
| `GET` | `/api/questions/{id}` | `getById` |
| `GET` | `/api/questions/digital-activity/{digitalActivityId}` | `getByDigitalActivity` |


## Referral

**Base Path:** `/api/referrals`

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/referrals/generate` | `generateReferralCode` |
| `POST` | `/api/referrals/apply` | `applyReferralCode` |
| `GET` | `/api/referrals/my-code` | `getMyReferralCode` |
| `GET` | `/api/referrals/usage` | `getReferralUsage` |


## ChildUser

**Base Path:** `/api/children`

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/children` | `create` |
| `GET` | `/api/children/{childId}` | `getById` |
| `PUT` | `/api/children/{childId}` | `update` |
| `DELETE` | `/api/children/{childId}` | `delete` |
| `GET` | `/api/children/phone/{phone}` | `getByPhone` |
| `GET` | `/api/children` | `unknown` |
| `GET` | `/api/children/parent/{parentId}` | `getChildrenByParent` |


## InteractiveProcessTracking

**Base Path:** `/api/interactive-process-tracking`

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/interactive-process-tracking/start` | `start` |
| `GET` | `/api/interactive-process-tracking/process/{processId}` | `get` |
| `PUT` | `/api/interactive-process-tracking/complete/{processId}` | `complete` |
| `GET` | `/api/interactive-process-tracking/child/{childId}` | `getByChild` |


## ParentUser

**Base Path:** `/api/parent-users`

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/parent-users` | `create` |
| `GET` | `/api/parent-users/{parentId}` | `getById` |
| `GET` | `/api/parent-users` | `unknown` |
| `PUT` | `/api/parent-users/{parentId}` | `update` |
| `DELETE` | `/api/parent-users/{parentId}` | `delete` |
| `GET` | `/api/parent-users/filter` | `byUserName` |
| `GET` | `/api/parent-users/phone` | `byPhone` |
| `GET` | `/api/parent-users/email` | `byEmail` |
| `GET` | `/api/parent-users/getPricingPlan` | `getPricingPlan` |


## InteractiveActivity

**Base Path:** `/api/interactive-activities`

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/interactive-activities` | `create` |
| `GET` | `/api/interactive-activities/by-submodule/{subModuleId}` | `getBySubModule` |
| `GET` | `/api/interactive-activities/{id}` | `getById` |
| `PUT` | `/api/interactive-activities/{id}` | `update` |
| `DELETE` | `/api/interactive-activities/{id}` | `delete` |


## ChildProgress

**Base Path:** `/api/progress`

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/progress/activity/start` | `startActivity` |
| `POST` | `/api/progress/activity/complete` | `completeActivity` |
| `POST` | `/api/progress/question/attempt` | `attempt` |
| `POST` | `/api/progress/digital/start` | `startDigital` |
| `POST` | `/api/progress/digital/complete` | `completeDigital` |
| `GET` | `/api/progress/child/{childId}/summary` | `getSummary` |
| `GET` | `/api/progress/child/{childId}/submodules` | `getSubModules` |
| `GET` | `/api/progress/child/{childId}/activities` | `getActivities` |
| `GET` | `/api/progress/child/{childId}/digitals` | `getDigitals` |
| `GET` | `/api/progress/school` | `getSchoolProgress` |
| `GET` | `/api/progress/children` | `getChildren` |
| `GET` | `/api/progress/location` | `getLocationProgress` |


## Auth

**Base Path:** `/api/auth`

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/auth/google` | `googleLogin` |
| `POST` | `/api/auth/test-login` | `testLogin` |
| `POST` | `/api/auth/refresh` | `refreshAccessToken` |


## QuestionOption

**Base Path:** `/api/question-options`

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/question-options` | `create` |
| `PUT` | `/api/question-options/{id}` | `update` |
| `DELETE` | `/api/question-options/{id}` | `delete` |
| `GET` | `/api/question-options/question/{questionId}` | `getByQuestion` |


## SubModule

**Base Path:** `/api/sub-modules`

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/sub-modules` | `create` |
| `GET` | `/api/sub-modules` | `getAll` |
| `GET` | `/api/sub-modules/by-module/{moduleId}` | `getByModule` |
| `GET` | `/api/sub-modules/{id}` | `getById` |
| `PUT` | `/api/sub-modules/{id}` | `update` |
| `DELETE` | `/api/sub-modules/{id}` | `delete` |


## PricingPlan

**Base Path:** `/api/pricing-plans`

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/pricing-plans` | `create` |
| `GET` | `/api/pricing-plans` | `getAll` |
| `GET` | `/api/pricing-plans/{id}` | `getById` |
| `PUT` | `/api/pricing-plans/{id}` | `update` |
| `DELETE` | `/api/pricing-plans/{id}` | `delete` |


## Wallet

**Base Path:** `/api/wallet`

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/wallet/{parentId}` | `getWallet` |
| `POST` | `/api/wallet/credit` | `credit` |
| `POST` | `/api/wallet/debit` | `debit` |
| `GET` | `/api/wallet/{parentId}/transactions` | `transactions` |


## Subject

**Base Path:** `/api/subjects`

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/subjects` | `create` |
| `GET` | `/api/subjects` | `getAll` |
| `GET` | `/api/subjects/{id}` | `getById` |
| `PUT` | `/api/subjects/{id}` | `update` |
| `DELETE` | `/api/subjects/{id}` | `delete` |


## Payment

**Base Path:** `/api/payments`

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/payments/create-order` | `createOrder` |
| `POST` | `/api/payments/verify` | `verifyPayment` |


## RazorpayWebhook

**Base Path:** `/api/webhooks/razorpay`

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/webhooks/razorpay` | `handleWebhook` |


## ParentQuiz

**Base Path:** `/api/v1/parent-quiz`

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/v1/parent-quiz` | `getQuizQuestions` |
| `POST` | `/api/v1/parent-quiz` | `addQuizQuestions` |
| `POST` | `/api/v1/parent-quiz/submit` | `submitQuiz` |


## ChildLifeSkill

**Base Path:** `/api/v1/child`

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/v1/child/{childId}/lifeskills` | `getChildLifeSkills` |


## ReportCard

**Base Path:** `/api/v1/report-card`

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/v1/report-card/child/{childId}` | `getMasterReportCard` |

