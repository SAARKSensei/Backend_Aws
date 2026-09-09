package com.sensei.backend.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.sensei.backend.enums.PaymentGateway;
import com.sensei.backend.enums.PaymentMethod;
import com.sensei.backend.enums.TransactionStatus;
import com.sensei.backend.enums.TransactionType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Column(name = "raw_response", columnDefinition = "text")
    private String rawResponse;



    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType transactionType;

    @Column(name = "coupon_code")
    private String couponCode;

    @Column(name = "coupon_discount")
    private Integer couponDiscount;


}
