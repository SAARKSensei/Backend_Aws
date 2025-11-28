package com.sensei.backend.controller;

import com.sensei.backend.dto.TransactionRequest;
import com.sensei.backend.entity.TransactionMaster;
import com.sensei.backend.enums.TransactionStatus;
import com.sensei.backend.enums.TransactionType;
import com.sensei.backend.service.TransactionMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
public class TransactionMasterController {

    @Autowired
    private TransactionMasterService transactionService;

    @PostMapping("/create")
    public ResponseEntity<?> createTransaction(@RequestBody TransactionRequest request) {
        try {
            TransactionMaster transaction = transactionService.createTransaction(request);
            
            Map<String, Object> response = new HashMap<>();
            response.put("transactionId", transaction.getTransactionId());
            response.put("status", transaction.getStatus());
            response.put("amount", transaction.getAmount());
            response.put("finalAmount", transaction.getFinalAmount());
            response.put("message", "Transaction created successfully");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{transactionId}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable String transactionId,
            @RequestParam TransactionStatus status,
            @RequestParam(required = false) String failureMessage) {
        try {
            TransactionMaster updated = transactionService.updateTransactionStatus(
                transactionId, status, failureMessage);
            
            return ResponseEntity.ok(Map.of(
                "transactionId", updated.getTransactionId(),
                "status", updated.getStatus(),
                "message", "Status updated successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{transactionId}/refund")
    public ResponseEntity<?> refundTransaction(
            @PathVariable String transactionId,
            @RequestParam String reason) {
        try {
            TransactionMaster refund = transactionService.recordRefund(transactionId, reason);
            
            return ResponseEntity.ok(Map.of(
                "refundTransactionId", refund.getTransactionId(),
                "amount", refund.getAmount(),
                "status", refund.getStatus(),
                "message", "Refund processed successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/child/{childId}")
    public ResponseEntity<?> getChildTransactions(@PathVariable String childId) {
        List<TransactionMaster> transactions = transactionService.getChildTransactions(childId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/parent/{parentId}")
    public ResponseEntity<?> getParentTransactions(@PathVariable String parentId) {
        List<TransactionMaster> transactions = transactionService.getParentTransactions(parentId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/gateway/{gatewayTxnId}")
    public ResponseEntity<?> getByGatewayId(@PathVariable String gatewayTxnId) {
        try {
            TransactionMaster transaction = transactionService.getByGatewayTransactionId(gatewayTxnId);
            return ResponseEntity.ok(transaction);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/revenue")
    public ResponseEntity<?> getRevenue(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        BigDecimal totalRevenue = transactionService.getTotalRevenue(startDate, endDate);
        
        Map<String, Object> report = new HashMap<>();
        report.put("startDate", startDate);
        report.put("endDate", endDate);
        report.put("totalRevenue", totalRevenue);
        report.put("currency", "INR");
        
        Map<String, BigDecimal> byType = new HashMap<>();
        for (TransactionType type : TransactionType.values()) {
            BigDecimal typeRevenue = transactionService.getRevenueByType(type);
            if (typeRevenue.compareTo(BigDecimal.ZERO) > 0) {
                byType.put(type.name(), typeRevenue);
            }
        }
        report.put("revenueByType", byType);
        
        return ResponseEntity.ok(report);
    }

    @GetMapping("/child/{childId}/has-plan")
    public ResponseEntity<?> hasPlan(@PathVariable String childId) {
        boolean hasPlan = transactionService.hasPurchasedPlan(childId);
        return ResponseEntity.ok(Map.of(
            "childId", childId,
            "hasPurchasedPlan", hasPlan
        ));
    }
}
