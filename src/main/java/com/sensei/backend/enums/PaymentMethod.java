package com.sensei.backend.enums;

public enum PaymentMethod {
    CARD("Credit/Debit Card"),
    UPI("UPI"),
    NETBANKING("Net Banking"),
    BANK_TRANSFER("Bank Transfer"),
    WALLET("Wallet Payment"),
    CASH("Cash (Offline)"),
    EMI("EMI"),
    OTHER("Other");

    private final String description;

    PaymentMethod(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
