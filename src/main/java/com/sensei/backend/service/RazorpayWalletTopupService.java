package com.sensei.backend.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.sensei.backend.entity.PaymentTransaction;
import com.sensei.backend.enums.PaymentGateway;
import com.sensei.backend.enums.TransactionStatus;
import com.sensei.backend.enums.TransactionType;
import com.sensei.backend.repository.PaymentTransactionRepository;

import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RazorpayWalletTopupService {

    private final RazorpayClient razorpayClient;
    private final PaymentTransactionRepository paymentRepo;

    public PaymentTransaction createWalletTopupOrder(int amountInRupees, UUID parentId) throws Exception {

        JSONObject options = new JSONObject();
        options.put("amount", amountInRupees * 100); // paise
        options.put("currency", "INR");
        options.put("receipt", "wallet_topup_" + System.currentTimeMillis());

        Order order = razorpayClient.orders.create(options);

        PaymentTransaction txn = PaymentTransaction.builder()
                .gateway(PaymentGateway.RAZORPAY)
                .gatewayOrderId(order.get("id"))
                .amount(amountInRupees)
                .currency("INR")
                .status(TransactionStatus.PENDING)
                .parentId(parentId)
                .transactionType(TransactionType.WALLET_TOPUP)
                .rawResponse(order.toString())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return paymentRepo.save(txn);
    }
}
