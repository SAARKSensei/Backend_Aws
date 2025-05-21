package com.sensei.backend.controller;

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
