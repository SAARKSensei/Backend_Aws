package com.sensei.backend.service;

import java.util.UUID;

public interface ReferralService {

    /**
     * Generate (or fetch existing) referral code for a parent.
     * Called after signup / first login.
     */
    String generateReferralCode(UUID parentId);

    /**
     * Apply a referral code when a new parent signs up.
     * Credits wallet for both referrer and referred user.
     */
    void applyReferralCode(UUID referredParentId, String referralCode);
}
