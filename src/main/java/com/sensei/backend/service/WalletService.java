package com.sensei.backend.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;
import com.sensei.backend.entity.Wallet;
import com.sensei.backend.entity.WalletTransaction;
import com.sensei.backend.repository.WalletRepository;
import com.sensei.backend.repository.WalletTransactionRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class WalletService {

    // inject the same names you put in application.properties
    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    private final RazorpayClient client;

    // repositories
    private final WalletRepository walletRepository;
    private final WalletTransactionRepository transactionRepository;

    @Autowired
    public WalletService(WalletRepository walletRepository,
                         WalletTransactionRepository transactionRepository,
                         @Value("${razorpay.key.id}") String keyId,
                         @Value("${razorpay.key.secret}") String keySecret) throws Exception {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
        this.keyId = keyId;
        this.keySecret = keySecret;
        this.client = new RazorpayClient(this.keyId, this.keySecret);
    }

    // create order - amount is passed as rupees (e.g. 200)
    public Map<String, Object> createOrder(BigDecimal amount) {
        try {
            int amountInPaise = amount.multiply(BigDecimal.valueOf(100)).intValue(); // 200 -> 20000

            Map<String, Object> options = new HashMap<>();
            options.put("amount", amountInPaise);
            options.put("currency", "INR");
            options.put("receipt", "txn_" + System.currentTimeMillis());

            Order order = client.orders.create((JSONObject) options); // if your SDK has different capitalization, use Order order = client.orders.create(options);

            Map<String, Object> response = new HashMap<>();
            response.put("orderId", order.get("id"));
            response.put("amount", amountInPaise);
            response.put("currency", "INR");
            response.put("status", order.get("status"));
            return response;
        } catch (Exception e) {
            throw new RuntimeException("Error creating Razorpay order: " + e.getMessage(), e);
        }
    }

    // verify payment: pass userId, orderId, paymentId, signature, and amount (in rupees)
    public Map<String, Object> verifyPayment(String userId,
                                             String razorpayOrderId,
                                             String razorpayPaymentId,
                                             String razorpaySignature,
                                             BigDecimal amount) {
        Map<String, Object> result = new HashMap<>();
        try {
            JSONObject attributes = new JSONObject();
            attributes.put("razorpay_order_id", razorpayOrderId);
            attributes.put("razorpay_payment_id", razorpayPaymentId);
            attributes.put("razorpay_signature", razorpaySignature);

            // THIS is the correct call: pass a JSONObject + your secret from properties
            Utils.verifyPaymentSignature(attributes, this.keySecret);

            // signature valid → add money to wallet
            addAmountAfterPayment(userId, amount);

            result.put("status", "success");
            result.put("message", "Payment verified successfully");
            return result;
        } catch (Exception e) {
            result.put("status", "failed");
            result.put("message", "Invalid payment signature: " + e.getMessage());
            return result;
        }
    }

    // add to wallet and record transaction
    public void addAmountAfterPayment(String userId, BigDecimal paymentAmount) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Wallet newWallet = new Wallet();
                    newWallet.setUserId(userId);
                    newWallet.setBalance(BigDecimal.ZERO);
                    return walletRepository.save(newWallet);
                });

        // negative-balance logic can be added here if needed
        wallet.setBalance(wallet.getBalance().add(paymentAmount));
        walletRepository.save(wallet);

        WalletTransaction transaction = new WalletTransaction();
        transaction.setWallet(wallet);
        transaction.setUserId(userId);
        transaction.setTransactionType("RAZORPAY_PAYMENT");
        transaction.setAmount(paymentAmount);
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setDescription("Payment added via Razorpay");
        transactionRepository.save(transaction);
    }
}
