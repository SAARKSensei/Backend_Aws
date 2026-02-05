package com.sensei.backend.service;

import java.util.UUID;

public interface PlanPurchaseService {

    void purchasePlan(
            UUID parentId,
            UUID childId,
            UUID pricingPlanId,
            int walletAmountUsed
    );
}
