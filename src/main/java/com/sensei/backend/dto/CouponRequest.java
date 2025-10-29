package com.sensei.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CouponRequest {

    private String code;
    private BigDecimal discountAmount;
    private boolean isPercentage;
    private LocalDateTime expirationDate;
    private String userId;

    // Getters & Setters
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public BigDecimal getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }

    public boolean isPercentage() { return isPercentage; }
    public void setPercentage(boolean isPercentage) { this.isPercentage = isPercentage; }

    public LocalDateTime getExpirationDate() { return expirationDate; }
    public void setExpirationDate(LocalDateTime expirationDate) { this.expirationDate = expirationDate; }

    public String getuserId() { return userId; }
    public void setuserId(String userId) { this.userId = userId; }
}
