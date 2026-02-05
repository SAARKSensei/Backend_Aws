package com.sensei.backend.service.impl;

import com.sensei.backend.dto.wallet.WalletDebitRequestDTO;
import com.sensei.backend.entity.PricingPlan;
import com.sensei.backend.repository.PricingPlanRepository;
import com.sensei.backend.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlanPurchaseServiceImpl implements PlanPurchaseService {

    private final PricingPlanRepository pricingPlanRepository;
    private final WalletService walletService;
    private final ChildPlanActivationService childPlanActivationService;

    @Override
    @Transactional
    public void purchasePlan(
            UUID parentId,
            UUID childId,
            UUID pricingPlanId,
            int walletAmountUsed
    ) {

        PricingPlan plan = pricingPlanRepository.findById(pricingPlanId)
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        int planPrice = plan.getPrice();

        // 1️⃣ Wallet debit (partial or full)
        if (walletAmountUsed > 0) {
            walletService.debit(
                WalletDebitRequestDTO.builder()
                    .parentId(parentId)
                    .amount(walletAmountUsed)
                    .pricingPlanId(pricingPlanId)
                    .transactionType("PLAN_PURCHASE")
                    .remarks("Wallet used for plan purchase")
                    .build()
            );
        }

        // 2️⃣ Remaining handled by Razorpay (already implemented)

        // 3️⃣ Activate plan
        childPlanActivationService.activatePlan(childId, pricingPlanId);
    }
}
