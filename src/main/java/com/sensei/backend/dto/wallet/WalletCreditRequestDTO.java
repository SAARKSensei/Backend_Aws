package com.sensei.backend.dto.wallet;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class WalletCreditRequestDTO {

    private UUID parentId;
    private Integer amount;

    private String transactionType; // REFERRAL_REWARD
    private String referenceType;   // REFERRAL
    private UUID referenceId;

    private String remarks;
}
