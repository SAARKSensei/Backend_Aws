package com.sensei.backend.enums;

public enum TransactionStatus {
    PENDING("Payment Initiated"),
    SUCCESS("Payment Successful"),
    FAILED("Payment Failed"),
    REFUNDED("Amount Refunded"),
    CANCELLED("Transaction Cancelled"),
    DISPUTED("Under Dispute"),
    REVERSED("Payment Reversed"),
    PROCESSING("Processing");

    private final String description;

    TransactionStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
