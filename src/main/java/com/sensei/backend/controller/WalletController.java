// package com.sensei.backend.controller;

// import com.sensei.backend.dto.WalletPaymentRequest;
// import com.sensei.backend.entity.WalletTransaction;
// import com.sensei.backend.repository.WalletTransactionRepository;
// import com.sensei.backend.service.WalletService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import java.math.BigDecimal;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;

// @RestController
// @RequestMapping("/api/wallet")
// public class WalletController {

//     @Autowired
//     private WalletService walletService;

//     @Autowired
//     private WalletTransactionRepository transactionRepository;

//     /**
//      * Create Razorpay order
//      */
//     @PostMapping("/payment/createOrder")
//     public ResponseEntity<?> createOrder(@RequestParam BigDecimal amount) {
//         try {
//             Map<String, Object> order = walletService.createOrder(amount);
//             return ResponseEntity.ok(order);
//         } catch (Exception e) {
//             return ResponseEntity.badRequest()
//                 .body(Map.of("error", e.getMessage()));
//         }
//     }

//     /**
//      * ✅ Verify payment (uses dual tracking)
//      */
//     @PostMapping("/payment/verify")
//     public ResponseEntity<?> verifyPayment(@RequestBody WalletPaymentRequest request) {
//         Map<String, Object> result = walletService.verifyPayment(
//             request.getUserId(),
//             request.getRazorpayOrderId(),
//             request.getRazorpayPaymentId(),
//             request.getRazorpaySignature(),
//             request.getAmount()
//         );
//         return ResponseEntity.ok(result);
//     }

//     /**
//      * Get wallet balance
//      */
//     @GetMapping("/balance")
//     public ResponseEntity<?> getBalance(@RequestParam String userId) {
//         try {
//             BigDecimal balance = walletService.getBalance(userId);
//             return ResponseEntity.ok(Map.of(
//                 "userId", userId,
//                 "balance", balance
//             ));
//         } catch (Exception e) {
//             return ResponseEntity.badRequest()
//                 .body(Map.of("error", e.getMessage()));
//         }
//     }

//     /**
//      * Get wallet transaction history
//      */
//     @GetMapping("/transactions")
//     public ResponseEntity<?> getTransactions(@RequestParam String userId) {
//         List<WalletTransaction> transactions = 
//             transactionRepository.findByUserIdOrderByCreatedAtDesc(userId);
//         return ResponseEntity.ok(transactions);
//     }

//     /**
//      * ✅ UPDATED: Purchase plan with dual tracking
//      */
//     @PostMapping("/purchase-plan")
//     public ResponseEntity<?> purchasePlan(
//             @RequestParam String userId,
//             @RequestParam(required = false) String parentId,
//             @RequestParam String planType,
//             @RequestParam(required = false) String referralCode) {
//         try {
//             BigDecimal planCost;
            
//             if ("99".equals(planType)) {
//                 planCost = BigDecimal.valueOf(99);
//             } else if ("199".equals(planType)) {
//                 planCost = BigDecimal.valueOf(199);
//             } else {
//                 return ResponseEntity.badRequest()
//                     .body(Map.of("error", "Invalid plan type. Use '99' or '199'"));
//             }
            
//             // ✅ Use dual tracking method
//             walletService.deductMoneyWithTracking(
//                 userId,
//                 parentId != null ? parentId : userId,
//                 planCost,
//                 "plan_" + planType,
//                 "PLAN_" + planType,
//                 referralCode,
//                 BigDecimal.ZERO, // No discount for now
//                 "Purchased ₹" + planType + " plan"
//             );
            
//             BigDecimal newBalance = walletService.getBalance(userId);
            
//             return ResponseEntity.ok(Map.of(
//                 "message", "✅ Plan purchased successfully!",
//                 "planType", planType,
//                 "amountDeducted", planCost,
//                 "remainingBalance", newBalance
//             ));
            
//         } catch (RuntimeException e) {
//             return ResponseEntity.badRequest()
//                 .body(Map.of("error", e.getMessage()));
//         }
//     }
// }
package com.sensei.backend.controller;

import com.sensei.backend.dto.wallet.*;
import com.sensei.backend.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    // 🔹 Get or create wallet
    @GetMapping("/{parentId}")
    public ResponseEntity<WalletResponseDTO> getWallet(@PathVariable UUID parentId) {
        return ResponseEntity.ok(walletService.getOrCreateWallet(parentId));
    }

    // 🔹 Credit wallet (referral, cashback, topup)
    @PostMapping("/credit")
    public ResponseEntity<WalletResponseDTO> credit(
            @RequestBody WalletCreditRequestDTO dto
    ) {
        return ResponseEntity.ok(walletService.credit(dto));
    }

    // 🔹 Debit wallet (plan purchase)
    @PostMapping("/debit")
    public ResponseEntity<WalletResponseDTO> debit(
            @RequestBody WalletDebitRequestDTO dto
    ) {
        return ResponseEntity.ok(walletService.debit(dto));
    }

    // 🔹 Wallet transactions
    @GetMapping("/{parentId}/transactions")
    public ResponseEntity<List<WalletTransactionResponseDTO>> transactions(
            @PathVariable UUID parentId
    ) {
        return ResponseEntity.ok(walletService.getTransactions(parentId));
    }
}
