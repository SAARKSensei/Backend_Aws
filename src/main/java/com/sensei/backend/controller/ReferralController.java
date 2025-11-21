package com.sensei.backend.controller;

import com.sensei.backend.entity.ReferralCode;
import com.sensei.backend.service.ReferralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/referral")
public class ReferralController {

    @Autowired
    private ReferralService referralService;

    /**
     * Get user's own referral code details
     */
    @GetMapping("/my-code")
    public ResponseEntity<?> getMyReferralCode(@RequestParam String userId) {
        try {
            ReferralCode code = referralService.getUserReferralCode(userId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("code", code.getCode());
            response.put("usageCount", code.getUsageCount());
            response.put("maxUsage", code.getMaxUsageLimit());
            response.put("remainingUses", code.getMaxUsageLimit() - code.getUsageCount());
            response.put("totalEarned", code.getUsageCount() * 100); // ₹100 per use
            response.put("maxEarnings", code.getMaxUsageLimit() * 100); // ₹500 max
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Apply referral code (can also be used after registration)
     */
    @PostMapping("/apply")
    public ResponseEntity<?> applyReferralCode(
            @RequestParam String code,
            @RequestParam String userId) {
        try {
            String message = referralService.applyReferral(code, userId);
            return ResponseEntity.ok(Map.of("message", message));
            
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
}
