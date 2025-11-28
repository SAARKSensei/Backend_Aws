package com.sensei.backend.controller;

import com.sensei.backend.service.TransactionVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/verify")
public class TransactionVerificationController {

    @Autowired
    private TransactionVerificationService verificationService;

    /**
     * Verify wallet consistency for a user
     */
    @GetMapping("/wallet/{userId}")
    public ResponseEntity<?> verifyWallet(@PathVariable String userId) {
        Map<String, Object> result = verificationService.verifyWalletConsistency(userId);
        return ResponseEntity.ok(result);
    }

    /**
     * Get detailed transaction comparison
     */
    @GetMapping("/transactions/{userId}")
    public ResponseEntity<?> compareTransactions(@PathVariable String userId) {
        Map<String, Object> result = verificationService.getTransactionComparison(userId);
        return ResponseEntity.ok(result);
    }
}
