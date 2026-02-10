package com.sensei.backend.service;
import java.util.List;

import java.util.UUID;

import com.sensei.backend.dto.referral.ReferralCodeResponseDTO;
import com.sensei.backend.dto.referral.ReferralUsageResponseDTO;

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


     // ✅ NEW
    ReferralCodeResponseDTO getReferralCodeForParent(UUID parentId);

    // ✅ NEW (analytics)
    List<ReferralUsageResponseDTO> getReferralUsage(UUID parentId);
}
