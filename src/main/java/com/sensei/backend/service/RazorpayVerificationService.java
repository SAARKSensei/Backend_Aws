package com.sensei.backend.service;

import com.razorpay.Utils;
import com.sensei.backend.entity.MasterTransaction;
import com.sensei.backend.entity.PaymentTransaction;
import com.sensei.backend.enums.TransactionStatus;
import com.sensei.backend.enums.TransactionType;
import com.sensei.backend.repository.MasterTransactionRepository;
import com.sensei.backend.repository.PaymentTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RazorpayVerificationService {

    private final PaymentTransactionRepository paymentRepo;
    private final MasterTransactionRepository masterRepo;
    private final ChildPlanActivationService planActivationService;

    public void verifyAndActivate(
            String orderId,
            String paymentId,
            String signature,
            String secret
    ) throws Exception {

        PaymentTransaction txn =
                paymentRepo.findByGatewayOrderId(orderId)
                        .orElseThrow(() -> new RuntimeException("Order not found"));

        String payload = orderId + "|" + paymentId;
        Utils.verifySignature(payload, signature, secret);

        txn.setGatewayPaymentId(paymentId);
        txn.setGatewaySignature(signature);
        txn.setStatus(TransactionStatus.SUCCESS);
        txn.setUpdatedAt(LocalDateTime.now());

        paymentRepo.save(txn);

        MasterTransaction master = MasterTransaction.builder()
                .transactionType(TransactionType.PLAN_PURCHASE)
                .transactionStatus(TransactionStatus.SUCCESS)
                .amount(txn.getAmount())
                .currency(txn.getCurrency())
                .childId(txn.getChildId())
                .parentId(txn.getParentId())
                .pricingPlanId(txn.getPricingPlanId())
                .paymentTransactionId(txn.getId())
                .createdAt(LocalDateTime.now())
                .remarks("Plan purchase via Razorpay")
                .build();

        masterRepo.save(master);

        // 🔥 ACTIVATE PLAN
        planActivationService.activatePlan(
                txn.getChildId(),
                txn.getPricingPlanId()
        );
    }
}
