package com.sensei.backend.controller;

import com.sensei.backend.entity.PaymentTransaction;
import com.sensei.backend.service.RazorpayOrderService;
import com.sensei.backend.service.RazorpayVerificationService;
import com.sensei.backend.service.RazorpayWalletTopupService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments/razorpay")
@RequiredArgsConstructor
public class RazorpayController {

    private final RazorpayOrderService orderService;
    private final RazorpayVerificationService verificationService;
    private final RazorpayWalletTopupService walletTopupService;

    @Value("${razorpay.key.secret}")
    private String razorpaySecret;

    /**
     * 1️⃣ Create Razorpay order for PLAN PURCHASE
     * Amount is in RUPEES
     */
    @PostMapping("/order/plan")
    public ResponseEntity<PaymentTransaction> createPlanOrder(
            @RequestParam int amount,          // rupees
            @RequestParam UUID parentId,
            @RequestParam UUID childId,
            @RequestParam UUID pricingPlanId
    ) throws Exception {

        return ResponseEntity.ok(
                orderService.createOrder(
                        amount,            // rupees
                        childId,
                        parentId,
                        pricingPlanId,
                        null,               // couponCode
                        0                   // couponDiscount
                )
        );
    }

    /**
     * 2️⃣ Verify Razorpay payment & activate plan
     */
    @PostMapping("/verify/plan")
    public ResponseEntity<String> verifyPlanPayment(
            @RequestParam String orderId,
            @RequestParam String paymentId,
            @RequestParam String signature
    ) throws Exception {

        verificationService.verifyAndActivate(
                orderId,
                paymentId,
                signature,
                razorpaySecret
        );

        return ResponseEntity.ok("PLAN_PAYMENT_SUCCESS");
    }

    /**
     * 3️⃣ Create Razorpay order for WALLET TOP-UP
     */
    @PostMapping("/order/wallet")
    public ResponseEntity<Map<String, Object>> createWalletTopupOrder(
            @RequestParam int amount   // rupees
    ) throws Exception {

        var order = walletTopupService.createWalletTopupOrder(amount);

        return ResponseEntity.ok(
                Map.of(
                        "orderId", order.get("id"),
                        "amount", order.get("amount"),
                        "currency", order.get("currency")
                )
        );
    }
}
