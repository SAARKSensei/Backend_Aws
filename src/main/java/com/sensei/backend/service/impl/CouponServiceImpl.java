package com.sensei.backend.service.impl;

import com.sensei.backend.dto.coupon.*;
import com.sensei.backend.entity.Coupon;
import com.sensei.backend.entity.CouponUsage;
import com.sensei.backend.repository.CouponRepository;
import com.sensei.backend.repository.CouponUsageRepository;
import com.sensei.backend.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;
import java.util.List;



import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final CouponUsageRepository couponUsageRepository;

    // -------------------------------
    // CREATE COUPON (ADMIN)
    // -------------------------------
    @Override
    public void createCoupon(CreateCouponRequestDTO dto) {

        Coupon coupon = Coupon.builder()
                .code(dto.getCode().toUpperCase())
                .discountType(dto.getDiscountType())
                .discountValue(dto.getDiscountValue())
                .maxDiscount(dto.getMaxDiscount())
                .minOrderAmount(dto.getMinOrderAmount())
                .applicablePricingPlanId(dto.getApplicablePricingPlanId())
                .maxUsage(dto.getMaxUsage())
                .usedCount(0)
                .expiryDate(dto.getExpiryDate())
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();

        couponRepository.save(coupon);
    }

    // -------------------------------
    // VALIDATE COUPON
    // -------------------------------
    @Override
    public ValidateCouponResponseDTO validateCoupon(ValidateCouponRequestDTO dto) {

        Coupon coupon = couponRepository
                .findByCodeAndIsActiveTrue(dto.getCouponCode().toUpperCase())
                .orElse(null);

        if (coupon == null) {
            return invalid("Invalid coupon code");
        }

        if (coupon.getExpiryDate() != null &&
                coupon.getExpiryDate().isBefore(LocalDateTime.now())) {
            return invalid("Coupon expired");
        }

        if (coupon.getMaxUsage() != null &&
                coupon.getUsedCount() >= coupon.getMaxUsage()) {
            return invalid("Coupon usage limit reached");
        }

        if (couponUsageRepository.existsByCouponIdAndParentId(
                coupon.getId(), dto.getParentId())) {
            return invalid("Coupon already used");
        }

        if (coupon.getMinOrderAmount() != null &&
                dto.getOrderAmount() < coupon.getMinOrderAmount()) {
            return invalid("Order amount too low for this coupon");
        }

        if (coupon.getApplicablePricingPlanId() != null &&
                !coupon.getApplicablePricingPlanId().equals(dto.getPricingPlanId())) {
            return invalid("Coupon not applicable for this plan");
        }

        int discount;
        if ("PERCENT".equalsIgnoreCase(coupon.getDiscountType())) {
            discount = (dto.getOrderAmount() * coupon.getDiscountValue()) / 100;
            if (coupon.getMaxDiscount() != null) {
                discount = Math.min(discount, coupon.getMaxDiscount());
            }
        } else {
            discount = coupon.getDiscountValue();
        }

        discount = Math.min(discount, dto.getOrderAmount());

        return ValidateCouponResponseDTO.builder()
                .valid(true)
                .discountAmount(discount)
                .finalAmount(dto.getOrderAmount() - discount)
                .message("Coupon applied successfully")
                .build();
    }

    // -------------------------------
    // CONSUME COUPON (POST PAYMENT)
    // -------------------------------
    @Override
    @Transactional
    public void consumeCoupon(
            String couponCode,
            UUID parentId,
            UUID pricingPlanId,
            UUID masterTransactionId,
            Integer discountAmount
    ) {

        Coupon coupon = couponRepository
                .findByCodeAndIsActiveTrue(couponCode.toUpperCase())
                .orElseThrow(() -> new RuntimeException("Invalid coupon"));

        CouponUsage usage = CouponUsage.builder()
                .couponId(coupon.getId())
                .parentId(parentId)
                .pricingPlanId(pricingPlanId)
                .masterTransactionId(masterTransactionId)
                .discountAmount(discountAmount)
                .usedAt(LocalDateTime.now())
                .build();

        couponUsageRepository.save(usage);

        coupon.setUsedCount(coupon.getUsedCount() + 1);
        couponRepository.save(coupon);
    }

    private ValidateCouponResponseDTO invalid(String msg) {
        return ValidateCouponResponseDTO.builder()
                .valid(false)
                .message(msg)
                .build();
    }

    @Override
        public List<Coupon> getAllCoupons() {
            return couponRepository.findAll();
    }

    @Override
    public Coupon getCouponById(UUID id) {
        return couponRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Coupon not found"));
    }
    @Override
    @Transactional
    public void updateCoupon(UpdateCouponRequestDTO dto) {

    Coupon coupon = couponRepository.findById(dto.getId())
            .orElseThrow(() -> new RuntimeException("Coupon not found"));

    coupon.setCode(dto.getCode());
    coupon.setDiscountType(dto.getDiscountType());
    coupon.setDiscountValue(dto.getDiscountValue());
    coupon.setMaxDiscount(dto.getMaxDiscount());
    coupon.setMinOrderAmount(dto.getMinOrderAmount());
    coupon.setApplicablePricingPlanId(dto.getApplicablePricingPlanId());
    coupon.setMaxUsage(dto.getMaxUsage());
    coupon.setExpiryDate(dto.getExpiryDate());
    coupon.setIsActive(dto.getIsActive());

    couponRepository.save(coupon);
    }

    @Override
@Transactional
public void disableCoupon(UUID id) {

    Coupon coupon = couponRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Coupon not found"));

    coupon.setIsActive(false);
    couponRepository.save(coupon);
}

@Override
@Transactional
public void deleteCoupon(UUID id) {

    Coupon coupon = couponRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Coupon not found"));

    if (coupon.getUsedCount() != null && coupon.getUsedCount() > 0) {
        throw new RuntimeException("Cannot delete coupon that has been used");
    }

    couponRepository.delete(coupon);
}





}
