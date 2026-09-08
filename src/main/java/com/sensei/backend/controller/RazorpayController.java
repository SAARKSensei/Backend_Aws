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
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/payments/razorpay")
@RequiredArgsConstructor
@Slf4j
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
        log.info("Razorpay plan order created for childId: {}", childId);

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
    public ResponseEntity<Map<String, String>> verifyPlanPayment(
            @RequestParam String orderId,
            @RequestParam String paymentId,
            @RequestParam String signature
    ) throws Exception {
        log.info("Verifying Razorpay plan payment orderId: {}", orderId);

        verificationService.verifyAndActivate(
                orderId,
                paymentId,
                signature,
                razorpaySecret
        );

        return ResponseEntity.ok(Map.of("status", "SUCCESS", "message", "PLAN_PAYMENT_SUCCESS"));
    }

    /**
     * 3️⃣ Create Razorpay order for WALLET TOP-UP
     */
    @PostMapping("/order/wallet")
    public ResponseEntity<Map<String, Object>> createWalletTopupOrder(
            @RequestParam int amount,   // rupees
            @RequestParam UUID parentId
    ) throws Exception {
        log.info("Razorpay wallet topup created for amount: {}, parent: {}", amount, parentId);

        PaymentTransaction txn = walletTopupService.createWalletTopupOrder(amount, parentId);

        return ResponseEntity.ok(
                Map.of(
                        "orderId", txn.getGatewayOrderId(),
                        "amount", txn.getAmount(),
                        "currency", txn.getCurrency()
                )
        );
    }

    /**
     * 4️⃣ Verify Razorpay wallet topup payment & credit wallet
     */
    @PostMapping("/verify/wallet")
    public ResponseEntity<Map<String, String>> verifyWalletPayment(
            @RequestParam String orderId,
            @RequestParam String paymentId,
            @RequestParam String signature
    ) throws Exception {
        log.info("Verifying Razorpay wallet topup payment orderId: {}", orderId);

        verificationService.verifyAndTopupWallet(
                orderId,
                paymentId,
                signature,
                razorpaySecret
        );

        return ResponseEntity.ok(Map.of("status", "SUCCESS", "message", "WALLET_TOPUP_SUCCESS"));
    }
}
