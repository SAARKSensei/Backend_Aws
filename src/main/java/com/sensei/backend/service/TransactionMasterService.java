package com.sensei.backend.service;

import com.sensei.backend.dto.TransactionRequest;
import com.sensei.backend.entity.TransactionMaster;
import com.sensei.backend.enums.TransactionStatus;
import com.sensei.backend.enums.TransactionType;
import com.sensei.backend.repository.TransactionMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionMasterService {

    @Autowired
    private TransactionMasterRepository transactionRepository;

    @Transactional
    public TransactionMaster createTransaction(TransactionRequest request) {
        TransactionMaster transaction = new TransactionMaster();
        
        transaction.setChildId(request.getChildId());
        transaction.setParentId(request.getParentId());
        transaction.setAmount(request.getAmount());
        transaction.setTransactionType(request.getTransactionType());
        transaction.setPaymentMethod(request.getPaymentMethod());
        transaction.setPaymentGateway(request.getPaymentGateway());
        transaction.setGatewayTransactionId(request.getGatewayTransactionId());
        transaction.setProductId(request.getProductId());
        transaction.setProductType(request.getProductType());
        transaction.setReferralCode(request.getReferralCode());
        transaction.setNotes(request.getNotes());
        
        BigDecimal discount = request.getDiscountAmount() != null ? 
            request.getDiscountAmount() : BigDecimal.ZERO;
        transaction.setDiscountAmount(discount);
        transaction.setFinalAmount(request.getAmount().subtract(discount));
        
        transaction.setWalletCredit(
            request.getTransactionType() == TransactionType.WALLET_TOPUP ||
            request.getTransactionType() == TransactionType.REFERRAL_REWARD ||
            request.getTransactionType() == TransactionType.CASHBACK ||
            request.getTransactionType() == TransactionType.REFUND
        );
        
        transaction.setStatus(TransactionStatus.PENDING);
        
        return transactionRepository.save(transaction);
    }

    @Transactional
    public TransactionMaster updateTransactionStatus(String transactionId, 
                                                     TransactionStatus status, 
                                                     String failureMessage) {
        TransactionMaster transaction = transactionRepository.findById(transactionId)
            .orElseThrow(() -> new RuntimeException("Transaction not found: " + transactionId));
        
        transaction.setStatus(status);
        
        if (status == TransactionStatus.SUCCESS) {
            transaction.setSettlementDate(LocalDateTime.now());
        } else if (status == TransactionStatus.FAILED) {
            transaction.setFailureMessage(failureMessage);
        }
        
        return transactionRepository.save(transaction);
    }

    @Transactional
    public TransactionMaster recordRefund(String originalTransactionId, String reason) {
        TransactionMaster original = transactionRepository.findById(originalTransactionId)
            .orElseThrow(() -> new RuntimeException("Original transaction not found"));
        
        if (original.getStatus() != TransactionStatus.SUCCESS) {
            throw new RuntimeException("Can only refund successful transactions");
        }
        
        TransactionMaster refund = new TransactionMaster();
        refund.setChildId(original.getChildId());
        refund.setParentId(original.getParentId());
        refund.setAmount(original.getFinalAmount());
        refund.setFinalAmount(original.getFinalAmount());
        refund.setTransactionType(TransactionType.REFUND);
        refund.setPaymentGateway(original.getPaymentGateway());
        refund.setStatus(TransactionStatus.SUCCESS);
        refund.setWalletCredit(true);
        refund.setNotes("Refund for transaction: " + originalTransactionId + ". Reason: " + reason);
        
        original.setStatus(TransactionStatus.REFUNDED);
        transactionRepository.save(original);
        
        return transactionRepository.save(refund);
    }

    public List<TransactionMaster> getChildTransactions(String childId) {
        return transactionRepository.findByChildIdOrderByTransactionDateDesc(childId);
    }

    public List<TransactionMaster> getParentTransactions(String parentId) {
        return transactionRepository.findByParentIdOrderByTransactionDateDesc(parentId);
    }

    public TransactionMaster getByGatewayTransactionId(String gatewayTxnId) {
        return transactionRepository.findByGatewayTransactionId(gatewayTxnId)
            .orElseThrow(() -> new RuntimeException("Transaction not found for gateway ID: " + gatewayTxnId));
    }

    public BigDecimal getTotalRevenue(LocalDateTime startDate, LocalDateTime endDate) {
        BigDecimal revenue = transactionRepository.getTotalRevenue(startDate, endDate);
        return revenue != null ? revenue : BigDecimal.ZERO;
    }

    public BigDecimal getRevenueByType(TransactionType type) {
        BigDecimal revenue = transactionRepository.getRevenueByType(type);
        return revenue != null ? revenue : BigDecimal.ZERO;
    }

    public boolean hasPurchasedPlan(String childId) {
        List<TransactionMaster> transactions = transactionRepository
            .findByChildIdAndStatus(childId, TransactionStatus.SUCCESS);
        
        return transactions.stream()
            .anyMatch(t -> t.getTransactionType() == TransactionType.PLAN_PURCHASE ||
                          t.getTransactionType() == TransactionType.PURCHASE);
    }
}
