package com.sensei.backend.service.impl;

import com.sensei.backend.dto.wallet.WalletCreditRequestDTO;
import com.sensei.backend.entity.ReferralCode;
import com.sensei.backend.entity.ReferralUsage;
import com.sensei.backend.repository.ReferralCodeRepository;
import com.sensei.backend.repository.ReferralUsageRepository;
import com.sensei.backend.service.ReferralService;
import com.sensei.backend.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReferralServiceImpl implements ReferralService {

    private final ReferralCodeRepository referralCodeRepository;
    private final ReferralUsageRepository referralUsageRepository;
    private final WalletService walletService;

    private static final int MAX_REFERRAL_USES = 5;
    private static final int REFERRAL_REWARD_AMOUNT = 100;

    // ---------------------------------------
    // GENERATE REFERRAL CODE
    // ---------------------------------------
    @Override
    public String generateReferralCode(UUID parentId) {

        return referralCodeRepository
                .findByParentId(parentId)
                .map(ReferralCode::getCode)
                .orElseGet(() -> {

                    String code = generateReadableCode(parentId);

                    ReferralCode referralCode = ReferralCode.builder()
                            .parentId(parentId)
                            .code(code)
                            .maxUsage(MAX_REFERRAL_USES)
                            .usedCount(0)
                            .active(true)
                            .createdAt(LocalDateTime.now())
                            .build();

                    referralCodeRepository.save(referralCode);
                    return code;
                });
    }

    // ---------------------------------------
    // APPLY REFERRAL CODE
    // ---------------------------------------
    @Override
    @Transactional
    public void applyReferralCode(UUID referredParentId, String referralCodeValue) {

        ReferralCode referralCode =
                referralCodeRepository
                        .findByCodeAndActiveTrue(referralCodeValue)
                        .orElseThrow(() -> new RuntimeException("Invalid referral code"));

        if (referralCode.getUsedCount() >= referralCode.getMaxUsage()) {
            throw new RuntimeException("Referral limit reached");
        }

        if (referralCode.getParentId().equals(referredParentId)) {
            throw new RuntimeException("Cannot use your own referral code");
        }

        boolean alreadyUsed =
                referralUsageRepository.existsByReferralCodeIdAndReferredParentId(
                        referralCode.getId(),
                        referredParentId
                );

        if (alreadyUsed) {
            throw new RuntimeException("Referral already applied");
        }

        ReferralUsage usage = ReferralUsage.builder()
                .referralCodeId(referralCode.getId())
                .referrerParentId(referralCode.getParentId())
                .referredParentId(referredParentId)
                .rewardAmount(REFERRAL_REWARD_AMOUNT)
                .usedAt(LocalDateTime.now())
                .build();

        referralUsageRepository.save(usage);

        referralCode.setUsedCount(referralCode.getUsedCount() + 1);
        referralCodeRepository.save(referralCode);

        // Credit referrer
        walletService.credit(
                WalletCreditRequestDTO.builder()
                        .parentId(referralCode.getParentId())
                        .amount(REFERRAL_REWARD_AMOUNT)
                        .transactionType("REFERRAL_REWARD")
                        .referenceType("REFERRAL")
                        .referenceId(usage.getId())
                        .remarks("Referral reward credited")
                        .build()
        );

        // Credit referred user
        walletService.credit(
                WalletCreditRequestDTO.builder()
                        .parentId(referredParentId)
                        .amount(REFERRAL_REWARD_AMOUNT)
                        .transactionType("REFERRAL_REWARD")
                        .referenceType("REFERRAL")
                        .referenceId(usage.getId())
                        .remarks("Signup referral reward credited")
                        .build()
        );
    }

    private String generateReadableCode(UUID parentId) {
        return "SENSEI-" +
                parentId.toString().substring(0, 4).toUpperCase() +
                "-" + (int) (Math.random() * 9000 + 1000);
    }
}
