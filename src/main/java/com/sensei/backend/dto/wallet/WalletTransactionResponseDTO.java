package com.sensei.backend.dto.wallet;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class WalletTransactionResponseDTO {

    private UUID id;
    private Integer amount;
    private String direction; // CREDIT / DEBIT
    private String transactionType;
    private String referenceType;
    private UUID referenceId;
    private String remarks;
    private LocalDateTime createdAt;
}
