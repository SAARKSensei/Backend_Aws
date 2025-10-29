package com.sensei.backend.controller;

import com.sensei.backend.entity.ReferralCode;
import com.sensei.backend.service.ReferralService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/referral")
public class ReferralController {

    @Autowired
    private ReferralService referralService;

    // ✅ Create referral API
    @PostMapping("/create")
    public ResponseEntity<?> createReferral(
            @RequestParam String referrerUserId,
            @RequestParam String referredUserId,
            @RequestParam String code,
            @RequestParam(required = false, defaultValue = "false") Boolean isSchool) {

        ReferralCode referral = referralService.createReferral(
                referrerUserId, referredUserId, code, isSchool);

        return ResponseEntity.ok(referral);
    }



    // ✅ Apply referral API
    @PostMapping("/apply")
    public String applyReferral(
            @RequestParam String code,
            @RequestParam String referredUserId) {

        return referralService.applyReferral(code, referredUserId);
    }
}