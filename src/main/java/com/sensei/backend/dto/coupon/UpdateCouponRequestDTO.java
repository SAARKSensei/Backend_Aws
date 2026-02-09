package com.sensei.backend.dto.coupon;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class UpdateCouponRequestDTO {

    private UUID id;

    private String code;
    private String discountType;
    private Integer discountValue;

    private Integer maxDiscount;
    private Integer minOrderAmount;

    private UUID applicablePricingPlanId;

    private Integer maxUsage;
    private LocalDateTime expiryDate;
    private Boolean isActive;
}
