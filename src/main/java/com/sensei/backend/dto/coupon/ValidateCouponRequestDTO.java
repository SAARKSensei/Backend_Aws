package com.sensei.backend.dto.coupon;

import lombok.Data;
import java.util.UUID;

@Data
public class ValidateCouponRequestDTO {

    private String couponCode;
    private UUID parentId;
    private UUID pricingPlanId;
    private Integer orderAmount;
}
