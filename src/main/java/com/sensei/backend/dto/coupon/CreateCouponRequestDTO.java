package com.sensei.backend.dto.coupon;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CreateCouponRequestDTO {

    private String code;
    private String discountType; // FLAT / PERCENT
    private Integer discountValue;

    private Integer maxDiscount;
    private Integer minOrderAmount;

    private UUID applicablePricingPlanId;

    private Integer maxUsage;
    private LocalDateTime expiryDate;
}
