package com.sensei.backend.dto.wallet;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class WalletResponseDTO {

    private UUID walletId;
    private UUID parentId;
    private Integer balance;
    private String status;
}
