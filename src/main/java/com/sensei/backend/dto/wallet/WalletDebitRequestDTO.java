package com.sensei.backend.dto.wallet;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletDebitRequestDTO {

    private UUID parentId;
    private Integer amount;

    // For plan purchase
    private UUID pricingPlanId;

    // PLAN_PURCHASE / REFUND / ADJUSTMENT
    private String transactionType;

    private String remarks;
}
