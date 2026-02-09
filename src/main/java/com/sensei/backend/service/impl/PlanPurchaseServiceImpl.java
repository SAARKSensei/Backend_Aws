package com.sensei.backend.service.impl;

import com.sensei.backend.dto.coupon.ValidateCouponRequestDTO;
import com.sensei.backend.dto.coupon.ValidateCouponResponseDTO;
import com.sensei.backend.dto.planpurchase.PlanPurchaseRequestDTO;
import com.sensei.backend.dto.wallet.WalletDebitRequestDTO;
import com.sensei.backend.entity.PricingPlan;
import com.sensei.backend.repository.PricingPlanRepository;
import com.sensei.backend.service.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlanPurchaseServiceImpl implements PlanPurchaseService {

    private final PricingPlanRepository pricingPlanRepository;
    private final WalletService walletService;
    private final CouponService couponService;
    private final RazorpayOrderService razorpayOrderService;
    private final ChildPlanActivationService childPlanActivationService;

    @Override
    @Transactional
    public void purchasePlan(PlanPurchaseRequestDTO dto) {

        PricingPlan plan = pricingPlanRepository.findById(dto.getPricingPlanId())
                .orElseThrow(() -> new RuntimeException("Pricing plan not found"));

        int planPrice = plan.getPrice();
        int payableAmount = planPrice;
        int couponDiscount = 0;

        // 1️⃣ COUPON
        if (dto.getCouponCode() != null && !dto.getCouponCode().isBlank()) {

            ValidateCouponRequestDTO couponReq = new ValidateCouponRequestDTO();
            couponReq.setCouponCode(dto.getCouponCode());
            couponReq.setParentId(dto.getParentId());
            couponReq.setPricingPlanId(dto.getPricingPlanId());
            couponReq.setOrderAmount(planPrice);

            ValidateCouponResponseDTO couponRes =
                    couponService.validateCoupon(couponReq);

            if (!couponRes.getValid()) {
                throw new RuntimeException(couponRes.getMessage());
            }

            couponDiscount = couponRes.getDiscountAmount();
            payableAmount = couponRes.getFinalAmount();
        }

        // 2️⃣ WALLET
        if (dto.getWalletAmountUsed() != null && dto.getWalletAmountUsed() > 0) {

            if (dto.getWalletAmountUsed() > payableAmount) {
                throw new RuntimeException("Wallet usage exceeds payable amount");
            }

            walletService.debit(
                    WalletDebitRequestDTO.builder()
                            .parentId(dto.getParentId())
                            .amount(dto.getWalletAmountUsed())
                            .pricingPlanId(dto.getPricingPlanId())
                            .transactionType("PLAN_PURCHASE")
                            .remarks("Wallet used for plan purchase")
                            .build()
            );

            payableAmount -= dto.getWalletAmountUsed();
        }

        // 3️⃣ RAZORPAY
        if (payableAmount > 0) {
            try {
                razorpayOrderService.createOrder(
        payableAmount,
        dto.getChildId(),
        dto.getParentId(),
        dto.getPricingPlanId(),
        dto.getCouponCode(),
        couponDiscount
);

                return; // verification callback will activate plan
            } catch (Exception e) {
                throw new RuntimeException("Razorpay order creation failed", e);
            }
        }

        // 4️⃣ FREE / FULL WALLET PURCHASE
        childPlanActivationService.activatePlan(
                dto.getChildId(),
                dto.getPricingPlanId()
        );

        if (dto.getCouponCode() != null) {
            couponService.consumeCoupon(
                    dto.getCouponCode(),
                    dto.getParentId(),
                    dto.getPricingPlanId(),
                    null,
                    couponDiscount
            );
        }
    }
}
