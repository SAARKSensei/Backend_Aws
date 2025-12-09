/*package com.sensei.backend.controller;

import com.sensei.backend.dto.PaymentRequestDTO;
import com.sensei.backend.entity.Payment;
import com.sensei.backend.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/create-order")
    public Payment createOrder(@RequestBody PaymentRequestDTO paymentRequestDTO) {
        return paymentService.createOrder(paymentRequestDTO);
    }

    @PostMapping("/capture-payment")
    public void capturePayment(@RequestParam String orderId,
                               @RequestParam String paymentId,
                               @RequestParam String signature) {
        paymentService.verifyAndCapturePayment(orderId, paymentId, signature);
    }
}
*/
package com.sensei.backend.controller;

import com.sensei.backend.dto.PaymentRequestDTO;
import com.sensei.backend.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private WalletService walletService;  // ✅ Changed from PaymentService to WalletService

    /**
     * Create Razorpay order
     * Endpoint: POST /api/payments/create-order
     */
    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestBody PaymentRequestDTO paymentRequestDTO) {
        try {
            // ✅ Extract amount from DTO and convert to BigDecimal
            BigDecimal amount = BigDecimal.valueOf(paymentRequestDTO.getAmount());
            
            // ✅ Call WalletService.createOrder() which returns Map<String, Object>
            Map<String, Object> order = walletService.createOrder(amount);
            
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Verify and capture payment
     * Endpoint: POST /api/payments/capture-payment
     */
    @PostMapping("/capture-payment")
    public ResponseEntity<?> capturePayment(@RequestBody PaymentRequestDTO paymentRequestDTO) {
        try {
            // ✅ Call WalletService.verifyPayment() instead of verifyAndCapturePayment()
            Map<String, Object> result = walletService.verifyPayment(
                paymentRequestDTO.getUserId(),
                paymentRequestDTO.getOrderId(),
                paymentRequestDTO.getPaymentId(),
                paymentRequestDTO.getSignature(),
                BigDecimal.valueOf(paymentRequestDTO.getAmount())
            );
            
            // Check if payment was successful
            if ("success".equals(result.get("status"))) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.badRequest().body(result);
            }
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
}