package com.sensei.backend.service;

import com.razorpay.Utils;
import com.sensei.backend.entity.MasterTransaction;
import com.sensei.backend.entity.PaymentTransaction;
import com.sensei.backend.enums.TransactionStatus;
import com.sensei.backend.enums.TransactionType;
import com.sensei.backend.repository.MasterTransactionRepository;
import com.sensei.backend.repository.PaymentTransactionRepository;
import com.sensei.backend.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RazorpayVerificationService {

    private final PaymentTransactionRepository paymentRepo;
    private final MasterTransactionRepository masterRepo;
    private final ChildPlanActivationService planActivationService;
    private final CouponService couponService;
    private final WalletService walletService;

    @Transactional
    public void verifyAndActivate(
            String orderId,
            String paymentId,
            String signature,
            String secret
    ) throws Exception {

        PaymentTransaction txn =
                paymentRepo.findByGatewayOrderId(orderId)
                        .orElseThrow(() -> new RuntimeException("Order not found"));

        // 🛑 IDEMPOTENCY GUARD #1
        // If already processed successfully, DO NOTHING
        if (txn.getStatus() == TransactionStatus.SUCCESS) {
            return;
        }

        // 🔐 Verify Razorpay signature
        String payload = orderId + "|" + paymentId;
        Utils.verifySignature(payload, signature, secret);

        // ✅ Mark payment as SUCCESS
        txn.setGatewayPaymentId(paymentId);
        txn.setGatewaySignature(signature);
        txn.setStatus(TransactionStatus.SUCCESS);
        txn.setUpdatedAt(LocalDateTime.now());
        paymentRepo.save(txn);

        // 🛑 IDEMPOTENCY GUARD #2
        // Prevent duplicate master transactions
        boolean masterExists =
                masterRepo.existsByPaymentTransactionId(txn.getId());

        if (masterExists) {
            return;
        }

        // 🧾 Create master transaction
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

        // 🔥 Activate plan (safe to call once)
        planActivationService.activatePlan(
                txn.getChildId(),
                txn.getPricingPlanId()
        );

        // 🎟️ Consume coupon ONLY ONCE
        if (txn.getCouponCode() != null) {
            couponService.consumeCoupon(
                    txn.getCouponCode(),
                    txn.getParentId(),
                    txn.getPricingPlanId(),
                    master.getId(),
                    txn.getCouponDiscount()
            );
        }
    }

    @Transactional
    public void verifyAndTopupWallet(
            String orderId,
            String paymentId,
            String signature,
            String secret
    ) throws Exception {

        PaymentTransaction txn =
                paymentRepo.findByGatewayOrderId(orderId)
                        .orElseThrow(() -> new RuntimeException("Wallet Order not found"));

        if (txn.getStatus() == TransactionStatus.SUCCESS) {
            return; // Idempotency
        }

        String payload = orderId + "|" + paymentId;
        Utils.verifySignature(payload, signature, secret);

        txn.setGatewayPaymentId(paymentId);
        txn.setGatewaySignature(signature);
        txn.setStatus(TransactionStatus.SUCCESS);
        txn.setUpdatedAt(LocalDateTime.now());
        paymentRepo.save(txn);

        boolean masterExists =
                masterRepo.existsByPaymentTransactionId(txn.getId());

        if (masterExists) {
            return;
        }

        MasterTransaction master = MasterTransaction.builder()
                .transactionType(TransactionType.WALLET_TOPUP)
                .transactionStatus(TransactionStatus.SUCCESS)
                .amount(txn.getAmount())
                .currency(txn.getCurrency())
                .parentId(txn.getParentId())
                .paymentTransactionId(txn.getId())
                .createdAt(LocalDateTime.now())
                .remarks("Wallet topup via Razorpay")
                .build();

        masterRepo.save(master);

        walletService.credit(com.sensei.backend.dto.wallet.WalletCreditRequestDTO.builder()
                .parentId(txn.getParentId())
                .amount(txn.getAmount())
                .transactionType(TransactionType.WALLET_TOPUP.name())
                .referenceType("RAZORPAY_PAYMENT")
                .referenceId(master.getId())
                .remarks("Wallet Top-up")
                .build());
    }
}
