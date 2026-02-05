package com.sensei.backend.entity;

import com.sensei.backend.enums.PaymentGateway;
import com.sensei.backend.enums.PaymentMethod;
import com.sensei.backend.enums.TransactionStatus;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payment_transaction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentTransaction {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Enumerated(EnumType.STRING)
    private PaymentGateway gateway;

    private String gatewayOrderId;
    private String gatewayPaymentId;
    private String gatewaySignature;

    private Integer amount;
    private String currency;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private Boolean isEmi;
    private Integer emiMonths;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    private String failureReason;

    private UUID childId;
    private UUID parentId;
    private UUID pricingPlanId;

    @Column(name = "raw_response", columnDefinition = "jsonb")
    private String rawResponse;


    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
