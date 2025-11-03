package com.sensei.backend.controller;

import com.sensei.backend.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/wallet/payment")
public class WalletController {

    @Autowired
    private WalletService walletService;

    // ✅ Step 1: Create order (frontend calls this before opening Razorpay)
    @PostMapping("/createOrder")
    public Map<String, Object> createOrder(@RequestParam BigDecimal amount) {
        return walletService.createOrder(amount);
    }

    // ✅ Step 2: Verify payment (frontend calls this after Razorpay payment)
    @PostMapping("/verify")
    public Map<String, Object> verifyPayment(@RequestBody Map<String, Object> payload) {
        String userId = (String) payload.get("userId");
        String razorpayOrderId = (String) payload.get("razorpayOrderId");
        String razorpayPaymentId = (String) payload.get("razorpayPaymentId");
        String razorpaySignature = (String) payload.get("razorpaySignature");
        BigDecimal amount = new BigDecimal(payload.get("amount").toString());

        return walletService.verifyPayment(userId, razorpayOrderId, razorpayPaymentId, razorpaySignature, amount);
    }
}
