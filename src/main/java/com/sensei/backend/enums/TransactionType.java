package com.sensei.backend.enums;

public enum TransactionType {
    PURCHASE("Course/Plan Purchase"),
    SUBSCRIPTION("Subscription Renewal"),
    WALLET_TOPUP("Wallet Top-up"),
    REFERRAL_REWARD("Referral Bonus"),
    REFUND("Refund"),
    CASHBACK("Cashback/Incentive"),
    COMMISSION("Commission Payment"),
    SCHOLARSHIP("Scholarship Grant"),
    CAMPAIGN_REWARD("Campaign Reward"),
    PLAN_PURCHASE("Plan Purchase (₹99 or ₹199)");

    private final String description;

    TransactionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
