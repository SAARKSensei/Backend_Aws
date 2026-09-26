package com.sensei.backend.controller;
import jakarta.validation.Valid;


import com.sensei.backend.dto.planpurchase.PlanPurchaseRequestDTO;
import com.sensei.backend.entity.PaymentTransaction;
import com.sensei.backend.service.PlanPurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/plan-purchases")
@RequiredArgsConstructor
public class PlanPurchaseController {

    private final PlanPurchaseService planPurchaseService;

    @PostMapping
    public ResponseEntity<?> purchasePlan(
            @Valid @RequestBody PlanPurchaseRequestDTO dto
    ) {
        PaymentTransaction txn = planPurchaseService.purchasePlan(dto);
        
        if (txn != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "PAYMENT_REQUIRED");
            response.put("orderId", txn.getGatewayOrderId());
            response.put("amount", txn.getAmount());
            response.put("currency", txn.getCurrency());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.ok(Map.of("status", "SUCCESS", "message", "PLAN_ACTIVATED_SUCCESSFULLY"));
        }
    }
}
