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
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class WalletService {

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    private final RazorpayClient client;
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

    /**
     * Create a new wallet for user with ₹0 balance
     */
    @Transactional
    public Wallet createWallet(String userId) {
        // Check if wallet already exists
        if (walletRepository.findByUserId(userId).isPresent()) {
            throw new RuntimeException("Wallet already exists for user: " + userId);
        }
        
        Wallet wallet = new Wallet();
        wallet.setUserId(userId);
        wallet.setBalance(BigDecimal.ZERO);
        return walletRepository.save(wallet);
    }

    /**
     * Get user's wallet (or create if doesn't exist)
     */
    @Transactional
    public Wallet getOrCreateWallet(String userId) {
        return walletRepository.findByUserId(userId)
            .orElseGet(() -> {
                Wallet newWallet = new Wallet();
                newWallet.setUserId(userId);
                newWallet.setBalance(BigDecimal.ZERO);
                return walletRepository.save(newWallet);
            });
    }

    /**
     * Create Razorpay order for adding money to wallet
     * Amount is in rupees (e.g., 200 means ₹200)
     */
    public Map<String, Object> createOrder(BigDecimal amount) {
        try {
            // Convert rupees to paise (200 → 20000)
            int amountInPaise = amount.multiply(BigDecimal.valueOf(100)).intValue();

            JSONObject options = new JSONObject();
            options.put("amount", amountInPaise);
            options.put("currency", "INR");
            options.put("receipt", "wallet_txn_" + System.currentTimeMillis());

            Order order = client.orders.create(options);

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

    /**
     * Verify Razorpay payment and add money to wallet
     */
    @Transactional
    public Map<String, Object> verifyPayment(String userId,
                                             String razorpayOrderId,
                                             String razorpayPaymentId,
                                             String razorpaySignature,
                                             BigDecimal amount) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // Verify signature
            JSONObject attributes = new JSONObject();
            attributes.put("razorpay_order_id", razorpayOrderId);
            attributes.put("razorpay_payment_id", razorpayPaymentId);
            attributes.put("razorpay_signature", razorpaySignature);

            Utils.verifyPaymentSignature(attributes, this.keySecret);

            // Signature valid → Add money to wallet
            addMoneyToWallet(userId, amount, "RAZORPAY_PAYMENT", 
                "Payment added via Razorpay - Order: " + razorpayOrderId);

            result.put("status", "success");
            result.put("message", "Payment verified and ₹" + amount + " added to wallet");
            
        } catch (Exception e) {
            result.put("status", "failed");
            result.put("message", "Payment verification failed: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * Add money to wallet (internal method)
     */
    @Transactional
    public void addMoneyToWallet(String userId, BigDecimal amount, 
                                 String transactionType, String description) {
        Wallet wallet = getOrCreateWallet(userId);
        
        // Add to balance
        wallet.setBalance(wallet.getBalance().add(amount));
        walletRepository.save(wallet);
        
        // Record transaction
        WalletTransaction transaction = new WalletTransaction();
        transaction.setWallet(wallet);
        transaction.setUserId(userId);
        transaction.setTransactionType(transactionType);
        transaction.setAmount(amount);
        transaction.setDescription(description);
        transactionRepository.save(transaction);
    }

    /**
     * Add referral bonus (called by ReferralService)
     */
    @Transactional
    public void addReferralBonus(String userId, BigDecimal amount, String description) {
        addMoneyToWallet(userId, amount, "REFERRAL_BONUS", description);
    }

    /**
     * Deduct money from wallet (for plan purchase)
     */
    @Transactional
    public void deductFromWallet(String userId, BigDecimal amount, String description) {
        Wallet wallet = walletRepository.findByUserId(userId)
            .orElseThrow(() -> new RuntimeException("Wallet not found for user: " + userId));
        
        // Check sufficient balance
        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient wallet balance! Current: ₹" + 
                wallet.getBalance() + ", Required: ₹" + amount);
        }
        
        // Deduct from balance
        wallet.setBalance(wallet.getBalance().subtract(amount));
        walletRepository.save(wallet);
        
        // Record transaction (negative amount)
        WalletTransaction transaction = new WalletTransaction();
        transaction.setWallet(wallet);
        transaction.setUserId(userId);
        transaction.setTransactionType("PLAN_PURCHASE");
        transaction.setAmount(amount.negate()); // Negative value
        transaction.setDescription(description);
        transactionRepository.save(transaction);
    }

    /**
     * Get wallet balance
     */
    public BigDecimal getBalance(String userId) {
        return walletRepository.findByUserId(userId)
            .map(Wallet::getBalance)
            .orElse(BigDecimal.ZERO);
    }
    
}
