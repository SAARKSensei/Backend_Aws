package com.sensei.backend.entity;

import com.sensei.backend.enums.TransactionStatus;
import com.sensei.backend.enums.TransactionType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "master_transaction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MasterTransaction {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;

    private Integer amount;
    private String currency;

    private UUID childId;
    private UUID parentId;

    private UUID pricingPlanId;
    private UUID paymentTransactionId;

    private String remarks;

    private LocalDateTime createdAt;
}
