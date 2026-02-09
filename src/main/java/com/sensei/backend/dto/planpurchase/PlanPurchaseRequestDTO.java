package com.sensei.backend.dto.planpurchase;

import lombok.Data;
import java.util.UUID;

@Data
public class PlanPurchaseRequestDTO {

    private UUID parentId;
    private UUID childId;
    private UUID pricingPlanId;

    // optional
    private String couponCode;
    private Integer walletAmountUsed;
}
