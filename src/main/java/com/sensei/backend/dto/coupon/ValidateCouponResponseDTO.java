package com.sensei.backend.dto.coupon;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ValidateCouponResponseDTO {

    private Boolean valid;
    private Integer discountAmount;
    private Integer finalAmount;
    private String message;
}
