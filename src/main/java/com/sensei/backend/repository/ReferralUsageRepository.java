package com.sensei.backend.repository;

import com.sensei.backend.entity.ReferralUsage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReferralUsageRepository
        extends JpaRepository<ReferralUsage, UUID> {

    // Prevent same user from using same code twice
    boolean existsByReferralCodeIdAndReferredParentId(
            UUID referralCodeId,
            UUID referredParentId
    );

    // Count how many times a referral code has been used
    long countByReferralCodeId(UUID referralCodeId);

    // Get all referrals done by a code owner (analytics later)
    List<ReferralUsage> findByReferralCodeId(UUID referralCodeId);

    // Used to find referral usage for a user
    Optional<ReferralUsage> findByReferredParentId(UUID referredParentId);
}
