package com.sensei.backend.controller;

import com.sensei.backend.dto.UserRegistrationRequest;
import com.sensei.backend.entity.ReferralCode;
import com.sensei.backend.entity.Wallet;
import com.sensei.backend.service.ReferralService;
import com.sensei.backend.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private ReferralService referralService;

    @Autowired
    private WalletService walletService;

    // TODO: Inject your actual UserService here
    // @Autowired
    // private UserService userService;

    /**
     * Register new user with optional referral code
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationRequest request) {
        try {
            // TODO: Replace this with your actual user creation logic
            // User newUser = userService.createUser(request);
            // For now, generating a mock user ID
            String newUserId = "user_" + System.currentTimeMillis();
            
            // Step 1: Create wallet with ₹0 balance
            walletService.createWallet(newUserId);
            
            // Step 2: Generate unique referral code for this user
            ReferralCode userCode = referralService.createReferralCodeForUser(
                newUserId, 
                request.getName(), 
                false  // isSchool = false for regular users
            );
            
            // Step 3: Check if user provided a referral code
            String bonusMessage = "";
            if (request.getReferralCode() != null && !request.getReferralCode().trim().isEmpty()) {
                try {
                    bonusMessage = referralService.applyReferral(
                        request.getReferralCode().trim(), 
                        newUserId
                    );
                } catch (RuntimeException e) {
                    bonusMessage = "⚠️ Referral code error: " + e.getMessage();
                }
            }
            
            // Step 4: Get final wallet balance
            BigDecimal finalBalance = walletService.getBalance(newUserId);
            
            // Build response
            Map<String, Object> response = new HashMap<>();
            response.put("userId", newUserId);
            response.put("name", request.getName());
            response.put("email", request.getEmail());
            response.put("referralCode", userCode.getCode());
            response.put("walletBalance", finalBalance);
            response.put("message", bonusMessage.isEmpty() ? 
                "✅ Account created successfully!" : bonusMessage);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
