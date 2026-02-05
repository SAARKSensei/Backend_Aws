package com.sensei.backend.entity;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "wallet_transaction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletTransaction {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(name = "wallet_id", nullable = false)
    private UUID walletId;

    @Column(name = "parent_id", nullable = false)
    private UUID parentId;

    /**
     * CREDIT / DEBIT
     */
    @Column(nullable = false)
    private String direction;

    /**
     * Amount in rupees
     */
    @Column(nullable = false)
    private Integer amount;

    /**
     * REFERRAL_REWARD / PLAN_PURCHASE / WALLET_TOPUP
     */
    @Column(name = "transaction_type", nullable = false)
    private String transactionType;

    /**
     * PRICING_PLAN / REFERRAL / PAYMENT
     */
    @Column(name = "reference_type")
    private String referenceType;

    @Column(name = "reference_id")
    private UUID referenceId;

    private String remarks;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
