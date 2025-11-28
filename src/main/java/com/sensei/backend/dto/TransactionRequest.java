package com.sensei.backend.dto;

import com.sensei.backend.enums.PaymentGateway;
import com.sensei.backend.enums.PaymentMethod;
import com.sensei.backend.enums.TransactionType;

import java.math.BigDecimal;

public class TransactionRequest {

    private String childId;
    private String parentId;
    private BigDecimal amount;
    private TransactionType transactionType;
    private PaymentMethod paymentMethod;
    private PaymentGateway paymentGateway;
    private String gatewayTransactionId;
    private String productId;
    private String productType;
    private String referralCode;
    private BigDecimal discountAmount;
    private String notes;

    // Getters and Setters
    public String getChildId() { return childId; }
    public void setChildId(String childId) { this.childId = childId; }

    public String getParentId() { return parentId; }
    public void setParentId(String parentId) { this.parentId = parentId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public TransactionType getTransactionType() { return transactionType; }
    public void setTransactionType(TransactionType transactionType) { this.transactionType = transactionType; }

    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }

    public PaymentGateway getPaymentGateway() { return paymentGateway; }
    public void setPaymentGateway(PaymentGateway paymentGateway) { this.paymentGateway = paymentGateway; }

    public String getGatewayTransactionId() { return gatewayTransactionId; }
    public void setGatewayTransactionId(String gatewayTransactionId) { this.gatewayTransactionId = gatewayTransactionId; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getProductType() { return productType; }
    public void setProductType(String productType) { this.productType = productType; }

    public String getReferralCode() { return referralCode; }
    public void setReferralCode(String referralCode) { this.referralCode = referralCode; }

    public BigDecimal getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
