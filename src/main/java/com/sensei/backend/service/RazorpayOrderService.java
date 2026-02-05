package com.sensei.backend.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.sensei.backend.entity.PaymentTransaction;
import com.sensei.backend.enums.PaymentGateway;
import com.sensei.backend.enums.TransactionStatus;
import com.sensei.backend.repository.PaymentTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RazorpayOrderService {

    private final RazorpayClient razorpayClient;
    private final PaymentTransactionRepository paymentRepo;

    public PaymentTransaction createOrder(
            Integer amount,
            UUID childId,
            UUID parentId,
            UUID pricingPlanId
    ) throws Exception {

        JSONObject options = new JSONObject();
        options.put("amount", amount);
        options.put("currency", "INR");
        options.put("receipt", "rcpt_" + UUID.randomUUID());

        Order order = razorpayClient.orders.create(options);

        PaymentTransaction txn = PaymentTransaction.builder()
                .gateway(PaymentGateway.RAZORPAY)
                .gatewayOrderId(order.get("id"))
                .amount(amount)
                .currency("INR")
                .status(TransactionStatus.PENDING)
                .childId(childId)
                .parentId(parentId)
                .pricingPlanId(pricingPlanId)
                .rawResponse(order.toString())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return paymentRepo.save(txn);
    }
}
