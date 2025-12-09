package com.sensei.backend.enums;

public enum PaymentGateway {
    RAZORPAY("Razorpay"),
    STRIPE("Stripe"),
    PAYTM("PayTM"),
    PHONEPE("PhonePe"),
    GPAY("Google Pay"),
    PAYU("PayU"),
    CASHFREE("Cashfree"),
    INTERNAL_WALLET("Internal Wallet"),
    MANUAL("Manual Entry"),
    NONE("No Gateway");

    private final String description;

    PaymentGateway(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

