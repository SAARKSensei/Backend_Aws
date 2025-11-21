package com.sensei.backend.controller;

import com.sensei.backend.dto.WalletPaymentRequest;
import com.sensei.backend.entity.WalletTransaction;
import com.sensei.backend.repository.WalletTransactionRepository;
import com.sensei.backend.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @Autowired
    private WalletTransactionRepository transactionRepository;

    /**
     * Step 1: Create Razorpay order (frontend calls before opening payment gateway)
     */
    @PostMapping("/payment/createOrder")
    public ResponseEntity<?> createOrder(@RequestParam BigDecimal amount) {
        try {
            Map<String, Object> order = walletService.createOrder(amount);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Step 2: Verify payment (frontend calls after Razorpay payment success)
     */
    @PostMapping("/payment/verify")
    public ResponseEntity<?> verifyPayment(@RequestBody WalletPaymentRequest request) {
        Map<String, Object> result = walletService.verifyPayment(
            request.getUserId(),
            request.getRazorpayOrderId(),
            request.getRazorpayPaymentId(),
            request.getRazorpaySignature(),
            request.getAmount()
        );
        return ResponseEntity.ok(result);
    }

    /**
     * Get wallet balance
     */
    @GetMapping("/balance")
    public ResponseEntity<?> getBalance(@RequestParam String userId) {
        try {
            BigDecimal balance = walletService.getBalance(userId);
            Map<String, Object> response = new HashMap<>();
            response.put("userId", userId);
            response.put("balance", balance);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get transaction history
     */
    @GetMapping("/transactions")
    public ResponseEntity<?> getTransactions(@RequestParam String userId) {
        List<WalletTransaction> transactions = 
            transactionRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return ResponseEntity.ok(transactions);
    }

    /**
     * Purchase plan using wallet balance
     */
    @PostMapping("/purchase-plan")
    public ResponseEntity<?> purchasePlan(
            @RequestParam String userId,
            @RequestParam String planType) {  // "99" or "199"
        try {
            BigDecimal planCost;
            
            if ("99".equals(planType)) {
                planCost = BigDecimal.valueOf(99);
            } else if ("199".equals(planType)) {
                planCost = BigDecimal.valueOf(199);
            } else {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid plan type. Use '99' or '199'"));
            }
            
            // Deduct from wallet
            walletService.deductFromWallet(userId, planCost, 
                "Purchased ₹" + planType + " plan");
            
            // Get updated balance
            BigDecimal newBalance = walletService.getBalance(userId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "✅ Plan purchased successfully!");
            response.put("planType", planType);
            response.put("amountDeducted", planCost);
            response.put("remainingBalance", newBalance);
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
}
