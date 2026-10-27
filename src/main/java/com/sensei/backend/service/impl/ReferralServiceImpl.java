package com.sensei.backend.service.impl;

import com.sensei.backend.dto.referral.ReferralCodeResponseDTO;
import com.sensei.backend.dto.referral.ReferralUsageResponseDTO;
import com.sensei.backend.dto.wallet.WalletCreditRequestDTO;
import com.sensei.backend.entity.ReferralCode;
import com.sensei.backend.entity.ReferralUsage;
import com.sensei.backend.exception.ValidationException;
import com.sensei.backend.repository.ReferralCodeRepository;
import com.sensei.backend.repository.ReferralUsageRepository;
import com.sensei.backend.service.ReferralService;
import com.sensei.backend.service.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ReferralServiceImpl implements ReferralService {

    private final ReferralCodeRepository referralCodeRepository;
    private final ReferralUsageRepository referralUsageRepository;
    private final WalletService walletService;

    @Value("${app.referral.max-usage:10}")
    private int maxReferralUses;

    @Value("${app.referral.reward-amount:100}")
    private int referralRewardAmount;

    // ---------------------------------------
    // GET OR CREATE REFERRAL CODE
    // ---------------------------------------
    @Transactional
    public ReferralCode getOrCreateReferralCode(UUID parentId) {
        return referralCodeRepository
                .findByParentId(parentId)
                .orElseGet(() -> {
                    String code = generateUniqueReadableCode(parentId);

                    ReferralCode referralCode = ReferralCode.builder()
                            .parentId(parentId)
                            .code(code)
                            .maxUsage(maxReferralUses)
                            .usedCount(0)
                            .active(true)
                            .createdAt(LocalDateTime.now())
                            .build();

                    log.info("Auto-generated referral code '{}' for parent: {}", code, parentId);
                    return referralCodeRepository.save(referralCode);
                });
    }

    // ---------------------------------------
    // GENERATE REFERRAL CODE
    // ---------------------------------------
    @Override
    @Transactional
    public String generateReferralCode(UUID parentId) {
        return getOrCreateReferralCode(parentId).getCode();
    }

    // ---------------------------------------
    // APPLY REFERRAL CODE
    // ---------------------------------------
    @Override
    @Transactional
    public void applyReferralCode(UUID referredParentId, String referralCodeValue) {

        if (referralCodeValue == null || referralCodeValue.trim().isEmpty()) {
            throw new ValidationException("Referral code cannot be empty");
        }

        String normalizedCode = referralCodeValue.trim().toUpperCase();

        ReferralCode referralCode =
                referralCodeRepository
                        .findByCodeAndActiveTrue(normalizedCode)
                        .orElseThrow(() -> new ValidationException("Invalid referral code"));

        if (referralCode.getUsedCount() >= referralCode.getMaxUsage()) {
            throw new ValidationException("Referral limit reached");
        }

        if (referralCode.getParentId().equals(referredParentId)) {
            throw new ValidationException("Cannot use your own referral code");
        }

        boolean alreadyRedeemed =
                referralUsageRepository.existsByReferredParentId(referredParentId);

        if (alreadyRedeemed) {
            throw new ValidationException("You have already redeemed a referral code");
        }

        ReferralUsage usage = ReferralUsage.builder()
                .referralCodeId(referralCode.getId())
                .referrerParentId(referralCode.getParentId())
                .referredParentId(referredParentId)
                .rewardAmount(referralRewardAmount)
                .usedAt(LocalDateTime.now())
                .build();

        referralUsageRepository.save(usage);

        referralCode.setUsedCount(referralCode.getUsedCount() + 1);
        if (referralCode.getUsedCount() >= referralCode.getMaxUsage()) {
            referralCode.setActive(false);
        }
        referralCodeRepository.save(referralCode);

        // Credit referrer
        walletService.credit(
                WalletCreditRequestDTO.builder()
                        .parentId(referralCode.getParentId())
                        .amount(referralRewardAmount)
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
                        .amount(referralRewardAmount)
                        .transactionType("REFERRAL_REWARD")
                        .referenceType("REFERRAL")
                        .referenceId(usage.getId())
                        .remarks("Signup referral reward credited")
                        .build()
        );
    }

    private String generateUniqueReadableCode(UUID parentId) {
        String code;
        int attempts = 0;
        do {
            code = "SENSEI-" +
                    parentId.toString().substring(0, 4).toUpperCase() +
                    "-" + (int) (Math.random() * 9000 + 1000);
            attempts++;
        } while (referralCodeRepository.existsByCode(code) && attempts < 10);
        return code;
    }

    @Override
    @Transactional
    public ReferralCodeResponseDTO getReferralCodeForParent(UUID parentId) {
        ReferralCode code = getOrCreateReferralCode(parentId);

        return ReferralCodeResponseDTO.builder()
                .id(code.getId())
                .parentId(code.getParentId())
                .code(code.getCode())
                .usedCount(code.getUsedCount())
                .maxUsage(code.getMaxUsage())
                .active(code.getActive())
                .build();
    }

    @Override
    public List<ReferralUsageResponseDTO> getReferralUsage(UUID parentId) {
        return referralCodeRepository
                .findByParentId(parentId)
                .map(code -> referralUsageRepository
                        .findByReferralCodeId(code.getId())
                        .stream()
                        .map(usage -> ReferralUsageResponseDTO.builder()
                                .referralCodeId(usage.getReferralCodeId())
                                .referrerParentId(usage.getReferrerParentId())
                                .referredParentId(usage.getReferredParentId())
                                .rewardAmount(usage.getRewardAmount())
                                .usedAt(usage.getUsedAt())
                                .build()
                        )
                        .toList()
                )
                .orElseGet(List::of);
    }

}
