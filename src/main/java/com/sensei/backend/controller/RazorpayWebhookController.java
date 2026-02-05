package com.sensei.backend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/webhooks/razorpay")
public class RazorpayWebhookController {

    @PostMapping
    public void handleWebhook(@RequestBody String payload) {
        // For refunds, disputes, async confirmations
        log.info("Razorpay Webhook Payload: {}", payload);
    }
}
