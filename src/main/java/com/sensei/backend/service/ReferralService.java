package com.sensei.backend.service;

import com.sensei.backend.entity.ReferralCode;
import com.sensei.backend.entity.ReferralActivity;
import com.sensei.backend.repository.ReferralCodeRepository;
import com.sensei.backend.repository.ReferralActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class ReferralService {

    @Autowired
    private ReferralCodeRepository referralCodeRepository;

    @Autowired
    private ReferralActivityRepository referralActivityRepository;

    @Autowired
    private WalletService walletService;

    /**
     * Generate unique referral code for new user
     * Format: USERNAME + 4-digit random number (e.g., "SHAAN8472")
     */
    public String generateUniqueReferralCode(String userName) {
        // Extract only alphabets and convert to uppercase
        String baseCode = userName.toUpperCase().replaceAll("[^A-Z]", "");
        
        // If name has no alphabets, use "USER"
        if (baseCode.isEmpty()) {
            baseCode = "USER";
        }
        
        // Limit to first 6 characters
        if (baseCode.length() > 6) {
            baseCode = baseCode.substring(0, 6);
        }
        
        String code;
        Random random = new Random();
        
        // Keep generating until we find a unique code
        do {
            int randomNum = 1000 + random.nextInt(9000); // 4-digit number
            code = baseCode + randomNum;
        } while (referralCodeRepository.existsByCode(code));
        
        return code;
    }

    /**
     * Create referral code for a new user
     */
    @Transactional
    public ReferralCode createReferralCodeForUser(String userId, String userName, Boolean isSchool) {
        // Check if user already has a code
        if (referralCodeRepository.findByReferrerUserId(userId).isPresent()) {
            throw new RuntimeException("User already has a referral code!");
        }
        
        String code = generateUniqueReferralCode(userName);
        
        ReferralCode referralCode = new ReferralCode();
        referralCode.setReferrerUserId(userId);
        referralCode.setCode(code);
        referralCode.setUsageCount(0);
        referralCode.setMaxUsageLimit(5);
        referralCode.setIsSchool(isSchool != null ? isSchool : false);
        
        return referralCodeRepository.save(referralCode);
    }

    /**
     * Apply referral code when new user signs up
     * Gives ₹100 to both code owner and new user
     */
    @Transactional
    public String applyReferral(String code, String referredUserId) {
        
        // Step 1: Check if referral code exists
        ReferralCode referral = referralCodeRepository.findByCode(code)
            .orElseThrow(() -> new RuntimeException("Invalid referral code: " + code));
        
        // Step 2: Check if user already used ANY referral code
        boolean alreadyUsedCode = referralActivityRepository.existsByReferredUserId(referredUserId);
        if (alreadyUsedCode) {
            throw new RuntimeException("You have already used a referral code!");
        }
        
        // Step 3: Check if code has reached maximum usage (5 times)
        if (referral.getUsageCount() >= referral.getMaxUsageLimit()) {
            throw new RuntimeException(
                String.format("This referral code has reached its maximum usage limit (%d/%d)!",
                    referral.getUsageCount(), referral.getMaxUsageLimit())
            );
        }
        
        // Step 4: Give ₹100 to code owner (referrer)
        walletService.addReferralBonus(
            referral.getReferrerUserId(),
            BigDecimal.valueOf(100),
            "Referral bonus: User " + referredUserId + " used your code " + code
        );
        
        // Step 5: Give ₹100 to new user (referred)
        walletService.addReferralBonus(
            referredUserId,
            BigDecimal.valueOf(100),
            "Referral bonus: You used code " + code
        );
        
        // Step 6: Increment usage count
        referral.setUsageCount(referral.getUsageCount() + 1);
        referralCodeRepository.save(referral);
        
        // Step 7: Record activity in referral_activity table
        ReferralActivity activity = new ReferralActivity();
        activity.setReferralCodeId(referral.getId());
        activity.setReferredUserId(referredUserId);
        referralActivityRepository.save(activity);
        
        return String.format(
            "✅ Referral applied successfully! Both you and the code owner received ₹100. Code usage: %d/%d",
            referral.getUsageCount(),
            referral.getMaxUsageLimit()
        );
    }

    /**
     * Get user's referral code details
     */
    public ReferralCode getUserReferralCode(String userId) {
        return referralCodeRepository.findByReferrerUserId(userId)
            .orElseThrow(() -> new RuntimeException("No referral code found for user: " + userId));
    }
}
