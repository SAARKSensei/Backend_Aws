package com.sensei.backend.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RazorpayWalletTopupService {

    private final RazorpayClient razorpayClient;

    public Order createWalletTopupOrder(int amountInRupees) throws Exception {

        JSONObject options = new JSONObject();
        options.put("amount", amountInRupees * 100); // paise
        options.put("currency", "INR");
        options.put("receipt", "wallet_topup");

        return razorpayClient.orders.create(options);
    }
}
