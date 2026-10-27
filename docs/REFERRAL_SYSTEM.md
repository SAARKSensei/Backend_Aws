# 🎁 Referral System & Wallet Rewards

This document explains the technical architecture, configuration, workflow, and edge cases of the Referral & Wallet Reward system in Sensei Backend.

---

## 1. Overview & Mechanics

The referral system incentivizes users to invite friends by crediting wallet rewards to **both parties** upon signup:

- **Referrer (Inviter):** Receives ₹100 into their wallet when a friend signs up using their code.
- **Referred Friend (Invitee):** Receives ₹100 welcome bonus into their wallet immediately upon signup.
- **Usage Cap:** A single referral code can be redeemed up to **10 times** (configurable).
- **Auto-Deactivation:** Once a code reaches 10 uses, it is automatically marked inactive. Subsequent signups using this link will still proceed smoothly, but no wallet bonuses will be deposited.

---

## 2. Configuration (`.env`)

Referral rules are dynamically configurable via environment variables in `.env` without modifying Java code:

```env
# Referral Settings
REFERRAL_MAX_USAGE=10
REFERRAL_REWARD_AMOUNT=100
```

Mapped in `application.properties`:
```properties
app.referral.max-usage=${REFERRAL_MAX_USAGE:10}
app.referral.reward-amount=${REFERRAL_REWARD_AMOUNT:100}
```

Injected in `ReferralServiceImpl.java`:
```java
@Value("${app.referral.max-usage:10}")
private int maxReferralUses;

@Value("${app.referral.reward-amount:100}")
private int referralRewardAmount;
```

---

## 3. Database Schema

Defined in `schema.sql`:

### `referral_code`
| Column | Type | Constraints | Description |
|---|---|---|---|
| `id` | UUID | Primary Key | Unique ID |
| `parent_id` | UUID | Unique, Not Null | Parent owning this referral code (1 code per parent) |
| `code` | VARCHAR(255) | Unique, Not Null | Unique code format: `SENSEI-<UUID-Prefix>-<Random>` |
| `max_usage` | INT | Not Null | Max allowable redemptions (default `10`) |
| `used_count` | INT | Not Null | Number of times used |
| `is_active` | BOOLEAN | Not Null | Whether code can be redeemed |
| `created_at` | TIMESTAMP | | Code generation timestamp |

### `referral_usage`
| Column | Type | Constraints | Description |
|---|---|---|---|
| `id` | UUID | Primary Key | Unique redemption ID |
| `referral_code_id` | UUID | Not Null | Referral code ID |
| `referrer_parent_id` | UUID | Not Null | User who shared the code |
| `referred_parent_id` | UUID | Not Null | New user who redeemed the code |
| `reward_amount` | INT | Not Null | Amount deposited (e.g. 100) |
| `used_at` | TIMESTAMP | Not Null | Timestamp of redemption |

> **Unique Constraint:** `UNIQUE (referral_code_id, referred_parent_id)` prevents the same friend from redeeming the same referral code more than once.

---

## 4. End-to-End Workflow

```
┌─────────────────┐             ┌─────────────────────┐
│  Parent User A  │             │   Friend User B     │
└────────┬────────┘             └──────────┬──────────┘
         │                                 │
         │ 1. GET /api/referrals/my-code   │
         │    (returns "SENSEI-4B8C-7312") │
         │                                 │
         │ 2. Shares Invite Link ─────────>│ Opens link:
         │    sensei.org.in/signup?ref=... │ (Stores ref code)
         │                                 │
         │                                 │ 3. Signs in with Google:
         │                                 │    POST /api/auth/google
         │                                 │    ?idToken=...&referralCode=SENSEI-4B8C-7312
         │                                 │
         │         ┌───────────────────────┴────────────────────────┐
         │         │               Sensei Backend                   │
         │         │                                                │
         │         │ - Creates ParentUser B                         │
         │         │ - Validates SENSEI-4B8C-7312 (active & < 10)   │
         │         │ - Increments used_count (if 10, sets active=F) │
         │         │ - Inserts record in referral_usage             │
         │         │                                                │
         │<────────┼─ Credit ₹100 to Wallet A ("REFERRAL_REWARD")   │
         │         ├─ Credit ₹100 to Wallet B ("REFERRAL_REWARD") ──┼───────>
         │         │                                                │
         │         │ - Generates User B's own referral code         │
         │         │ - Returns Auth JWT Token ──────────────────────┼───────>
         │         └────────────────────────────────────────────────┘
```

---

## 5. API Reference

### A. Auth Integration (Recommended 1-Step Flow)
Pass the optional `referralCode` query parameter directly when logging in with Google:

- **Endpoint:** `POST /api/auth/google`
- **Parameters:**
  - `idToken` (String, required): Google OAuth id token
  - `client` (String, optional, default: `"web"`): `"web"` or `"app"`
  - `referralCode` (String, optional): Referral code e.g. `SENSEI-4B8C-7312`
- **Behavior:**
  - If it is a new user and `referralCode` is present, it auto-applies the code, credits ₹100 to both wallets, and creates a referral code for the new user.
  - If `referralCode` is omitted, login proceeds as normal organic signup (₹0 bonus).
  - If `referralCode` is exhausted or invalid, login **still succeeds**, but no bonus is deposited.

---

### B. Manual Referral Endpoints

#### 1. Fetch My Referral Code
- **Endpoint:** `GET /api/referrals/my-code`
- **Params:** `parentId=<UUID>`
- **Response:**
  ```json
  {
    "id": "c1f7a28e-324e-48a0-9721-5a1e20173201",
    "parentId": "d983416a-b289-4b21-827a-7bfca915b81a",
    "code": "SENSEI-D983-4921",
    "usedCount": 3,
    "maxUsage": 10,
    "active": true
  }
  ```

#### 2. Manual Apply Referral Code
- **Endpoint:** `POST /api/referrals/apply`
- **Params:** `parentId=<UUID>`, `referralCode=<string>`
- **Response:** `"Referral code applied successfully"`

#### 3. View Referral History & Analytics
- **Endpoint:** `GET /api/referrals/usage`
- **Params:** `parentId=<UUID>`
- **Response:**
  ```json
  [
    {
      "referralCodeId": "c1f7a28e-324e-48a0-9721-5a1e20173201",
      "referrerParentId": "d983416a-b289-4b21-827a-7bfca915b81a",
      "referredParentId": "a5839211-1200-4b2a-8c90-9fbb381a3821",
      "rewardAmount": 100,
      "usedAt": "2026-10-26T15:30:00"
    }
  ]
  ```

---

## 6. Edge Cases & Safeguards

| Scenario | Behavior | Result |
|---|---|---|
| **Organic signup (No link / No code)** | `referralCode` is null or empty | Account created normally; ₹0 bonus deposited; own referral code is still auto-generated. |
| **Self-Referral** | User tries to use their own referral code | Rejected with `"Cannot use your own referral code"`. |
| **Duplicate Redemption (Same or Another Code)** | User tries to redeem a code again or use a second friend's code | Rejected with `"You have already redeemed a referral code"`. Users can only ever be referred once. |
| **Limit Reached (11th user)** | Code has already reached `maxUsage` (10) | Rejected with `"Referral limit reached"`. User account is created successfully, but ₹0 bonus is credited to either user. |
| **Code Auto-Deactivation** | Code is used for the 10th time | `used_count` becomes 10 and `is_active` is updated to `false`. |
| **Missing Wallet** | User doesn't have an existing wallet row | `walletService.credit()` creates an active wallet row with balance 0 and credits ₹100. |
| **Atomic Guarantee** | Network or DB failure during deposit | Wrapped in `@Transactional`. If crediting one party fails, the entire transaction rolls back cleanly. |
