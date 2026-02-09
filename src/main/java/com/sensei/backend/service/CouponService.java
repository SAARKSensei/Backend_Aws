package com.sensei.backend.service;

import com.sensei.backend.dto.coupon.CreateCouponRequestDTO;
import com.sensei.backend.dto.coupon.UpdateCouponRequestDTO;
import com.sensei.backend.dto.coupon.ValidateCouponRequestDTO;
import com.sensei.backend.dto.coupon.ValidateCouponResponseDTO;
import com.sensei.backend.entity.Coupon;

import java.util.List;
import java.util.UUID;

public interface CouponService {

    void createCoupon(CreateCouponRequestDTO dto);

    ValidateCouponResponseDTO validateCoupon(ValidateCouponRequestDTO dto);

    void consumeCoupon(
            String couponCode,
            UUID parentId,
            UUID pricingPlanId,
            UUID masterTransactionId,
            Integer discountAmount
    );

    // ✅ ADMIN CRUD
    List<Coupon> getAllCoupons();

    Coupon getCouponById(UUID id);

    void updateCoupon(UpdateCouponRequestDTO dto);

    void disableCoupon(UUID id);

    void deleteCoupon(UUID id);
}
