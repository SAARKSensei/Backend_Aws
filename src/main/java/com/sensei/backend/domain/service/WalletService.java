package com.sensei.backend.domain.service;
// package com.sensei.backend.service;

// import com.razorpay.Order;
// import com.razorpay.RazorpayClient;
// import com.razorpay.Utils;
// import com.sensei.backend.dto.TransactionRequest;
// import com.sensei.backend.entity.TransactionMaster;
// import com.sensei.backend.entity.Wallet;
// import com.sensei.backend.entity.WalletTransaction;
// import com.sensei.backend.enums.PaymentGateway;
// import com.sensei.backend.enums.PaymentMethod;
// import com.sensei.backend.enums.TransactionStatus;
// import com.sensei.backend.enums.TransactionType;
// import com.sensei.backend.repository.WalletRepository;
// import com.sensei.backend.repository.WalletTransactionRepository;
// import org.json.JSONObject;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Service;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.transaction.annotation.Transactional;

// import java.math.BigDecimal;
// import java.time.LocalDateTime;
// import java.util.HashMap;
// import java.util.Map;

// @Service
// public class WalletService {

//     @Value("${razorpay.key.id}")
//     private String keyId;

//     @Value("${razorpay.key.secret}")
//     private String keySecret;

//     private final RazorpayClient client;
//     private final WalletRepository walletRepository;
//     private final WalletTransactionRepository transactionRepository;

//     @Autowired
//     private TransactionMasterService transactionMasterService; // ✅ NEW: For dual tracking

//     @Autowired
//     public WalletService(WalletRepository walletRepository,
//                          WalletTransactionRepository transactionRepository,
//                          @Value("${razorpay.key.id}") String keyId,
//                          @Value("${razorpay.key.secret}") String keySecret) throws Exception {
//         this.walletRepository = walletRepository;
//         this.transactionRepository = transactionRepository;
//         this.keyId = keyId;
//         this.keySecret = keySecret;
//         this.client = new RazorpayClient(this.keyId, this.keySecret);
//     }

//     // ==================== EXISTING METHODS (Keep as is) ====================

//     /**
//      * Create a new wallet for user with ₹0 balance
//      */
//     @Transactional
//     public Wallet createWallet(String userId) {
//         if (walletRepository.findByUserId(userId).isPresent()) {
//             throw new RuntimeException("Wallet already exists for user: " + userId);
//         }
        
//         Wallet wallet = new Wallet();
//         wallet.setUserId(userId);
//         wallet.setBalance(BigDecimal.ZERO);
//         return walletRepository.save(wallet);
//     }

//     /**
//      * Get user's wallet (or create if doesn't exist)
//      */
//     @Transactional
//     public Wallet getOrCreateWallet(String userId) {
//         return walletRepository.findByUserId(userId)
//             .orElseGet(() -> {
//                 Wallet newWallet = new Wallet();
//                 newWallet.setUserId(userId);
//                 newWallet.setBalance(BigDecimal.ZERO);
//                 return walletRepository.save(newWallet);
//             });
//     }

//     /**
//      * Create Razorpay order for adding money to wallet
//      */
//     public Map<String, Object> createOrder(BigDecimal amount) {
//         try {
//             int amountInPaise = amount.multiply(BigDecimal.valueOf(100)).intValue();

//             JSONObject options = new JSONObject();
//             options.put("amount", amountInPaise);
//             options.put("currency", "INR");
//             options.put("receipt", "wallet_txn_" + System.currentTimeMillis());

//             Order order = client.orders.create(options);

//             Map<String, Object> response = new HashMap<>();
//             response.put("orderId", order.get("id"));
//             response.put("amount", amountInPaise);
//             response.put("currency", "INR");
//             response.put("status", order.get("status"));
            
//             return response;
//         } catch (Exception e) {
//             throw new RuntimeException("Error creating Razorpay order: " + e.getMessage(), e);
//         }
//     }

//     /**
//      * Get wallet balance
//      */
//     public BigDecimal getBalance(String userId) {
//         return walletRepository.findByUserId(userId)
//             .map(Wallet::getBalance)
//             .orElse(BigDecimal.ZERO);
//     }

//     // ==================== INTERNAL METHODS (Keep for backward compatibility) ====================

//     /**
//      * Internal method: Add to wallet (wallet_transactions only)
//      * Keep for backward compatibility, but new code should use addMoneyWithTracking()
//      */
//     @Transactional
//     public void addMoneyToWallet(String userId, BigDecimal amount, 
//                                  String transactionType, String description) {
//         Wallet wallet = getOrCreateWallet(userId);
        
//         wallet.setBalance(wallet.getBalance().add(amount));
//         walletRepository.save(wallet);
        
//         WalletTransaction transaction = new WalletTransaction();
//         transaction.setWallet(wallet);
//         transaction.setUserId(userId);
//         transaction.setTransactionType(transactionType);
//         transaction.setAmount(amount);
//         transaction.setDescription(description);
//         transactionRepository.save(transaction);
//     }

//     /**
//      * Internal method: Deduct from wallet (wallet_transactions only)
//      * Keep for backward compatibility, but new code should use deductMoneyWithTracking()
//      */
//     @Transactional
//     public void deductFromWallet(String userId, BigDecimal amount, String description) {
//         Wallet wallet = walletRepository.findByUserId(userId)
//             .orElseThrow(() -> new RuntimeException("Wallet not found for user: " + userId));
        
//         if (wallet.getBalance().compareTo(amount) < 0) {
//             throw new RuntimeException("Insufficient wallet balance! Current: ₹" + 
//                 wallet.getBalance() + ", Required: ₹" + amount);
//         }
        
//         wallet.setBalance(wallet.getBalance().subtract(amount));
//         walletRepository.save(wallet);
        
//         WalletTransaction transaction = new WalletTransaction();
//         transaction.setWallet(wallet);
//         transaction.setUserId(userId);
//         transaction.setTransactionType("PLAN_PURCHASE");
//         transaction.setAmount(amount.negate());
//         transaction.setDescription(description);
//         transactionRepository.save(transaction);
//     }

//     // ==================== ✅ NEW DUAL-TRACKING METHODS ====================

//     /**
//      * ✅ Add money to wallet + record in transaction_master
//      * Use this for all new implementations
//      */
//     @Transactional
//     public TransactionMaster addMoneyWithTracking(
//             String userId, 
//             String parentId,
//             BigDecimal amount,
//             TransactionType transactionType,
//             PaymentGateway gateway,
//             PaymentMethod method,
//             String gatewayTxnId,
//             String productId,
//             String productType,
//             String referralCode,
//             String description) {
        
//         // 1. Update wallet balance and wallet_transactions
//         addMoneyToWallet(userId, amount, transactionType.name(), description);
        
//         // 2. Record in transaction_master
//         TransactionRequest txnRequest = new TransactionRequest();
//         txnRequest.setChildId(userId);
//         txnRequest.setParentId(parentId != null ? parentId : userId);
//         txnRequest.setAmount(amount);
//         txnRequest.setTransactionType(transactionType);
//         txnRequest.setPaymentGateway(gateway);
//         txnRequest.setPaymentMethod(method);
//         txnRequest.setGatewayTransactionId(gatewayTxnId);
//         txnRequest.setProductId(productId);
//         txnRequest.setProductType(productType);
//         txnRequest.setReferralCode(referralCode);
//         txnRequest.setDiscountAmount(BigDecimal.ZERO);
//         txnRequest.setNotes(description);
        
//         TransactionMaster transaction = transactionMasterService.createTransaction(txnRequest);
        
//         // 3. Mark as success
//         transactionMasterService.updateTransactionStatus(
//             transaction.getTransactionId(), 
//             TransactionStatus.SUCCESS, 
//             null
//         );
        
//         return transaction;
//     }

//     /**
//      * ✅ Overloaded version with fewer parameters (for referrals)
//      */
//     @Transactional
//     public TransactionMaster addMoneyWithTracking(
//             String userId,
//             String parentId,
//             BigDecimal amount,
//             TransactionType transactionType,
//             PaymentGateway gateway,
//             PaymentMethod method,
//             String gatewayTxnId,
//             String description) {
        
//         return addMoneyWithTracking(
//             userId, parentId, amount, transactionType, 
//             gateway, method, gatewayTxnId, 
//             null, null, null, description
//         );
//     }

//     /**
//      * ✅ Deduct money from wallet + record in transaction_master
//      * Use this for plan purchases
//      */
//     @Transactional
//     public TransactionMaster deductMoneyWithTracking(
//             String userId,
//             String parentId,
//             BigDecimal amount,
//             String productId,
//             String productType,
//             String referralCode,
//             BigDecimal discountAmount,
//             String description) {
        
//         // 1. Calculate final amount after discount
//         BigDecimal finalAmount = amount.subtract(discountAmount != null ? discountAmount : BigDecimal.ZERO);
        
//         // 2. Deduct from wallet and wallet_transactions
//         deductFromWallet(userId, finalAmount, description);
        
//         // 3. Record in transaction_master
//         TransactionRequest txnRequest = new TransactionRequest();
//         txnRequest.setChildId(userId);
//         txnRequest.setParentId(parentId != null ? parentId : userId);
//         txnRequest.setAmount(amount);
//         txnRequest.setDiscountAmount(discountAmount);
//         txnRequest.setTransactionType(TransactionType.PLAN_PURCHASE);
//         txnRequest.setPaymentGateway(PaymentGateway.INTERNAL_WALLET);
//         txnRequest.setPaymentMethod(PaymentMethod.WALLET);
//         txnRequest.setProductId(productId);
//         txnRequest.setProductType(productType);
//         txnRequest.setReferralCode(referralCode);
//         txnRequest.setNotes(description);
        
//         TransactionMaster transaction = transactionMasterService.createTransaction(txnRequest);
        
//         // 4. Mark as success
//         transactionMasterService.updateTransactionStatus(
//             transaction.getTransactionId(), 
//             TransactionStatus.SUCCESS, 
//             null
//         );
        
//         return transaction;
//     }

//     /**
//      * ✅ Overloaded version without discount
//      */
//     @Transactional
//     public TransactionMaster deductMoneyWithTracking(
//             String userId,
//             String parentId,
//             BigDecimal amount,
//             String productId,
//             String productType,
//             String description) {
        
//         return deductMoneyWithTracking(
//             userId, parentId, amount, 
//             productId, productType, 
//             null, BigDecimal.ZERO, description
//         );
//     }

//     // ==================== ✅ UPDATED PAYMENT VERIFICATION ====================

//     /**
//      * ✅ UPDATED: Verify Razorpay payment and add to wallet (dual tracking)
//      */
//     @Transactional
//     public Map<String, Object> verifyPayment(String userId,
//                                              String razorpayOrderId,
//                                              String razorpayPaymentId,
//                                              String razorpaySignature,
//                                              BigDecimal amount) {
//         Map<String, Object> result = new HashMap<>();
        
//         try {
//             // Verify signature
//             JSONObject attributes = new JSONObject();
//             attributes.put("razorpay_order_id", razorpayOrderId);
//             attributes.put("razorpay_payment_id", razorpayPaymentId);
//             attributes.put("razorpay_signature", razorpaySignature);

//             Utils.verifyPaymentSignature(attributes, this.keySecret);

//             // ✅ CHANGED: Use dual tracking
//             TransactionMaster transaction = addMoneyWithTracking(
//                 userId,
//                 userId, // parentId = userId (update when you have actual parent ID)
//                 amount,
//                 TransactionType.WALLET_TOPUP,
//                 PaymentGateway.RAZORPAY,
//                 PaymentMethod.UPI, // Default, can be extracted from Razorpay response
//                 razorpayPaymentId,
//                 "Wallet top-up via Razorpay - Order: " + razorpayOrderId
//             );

//             result.put("status", "success");
//             result.put("message", "Payment verified and ₹" + amount + " added to wallet");
//             result.put("transactionId", transaction.getTransactionId());
//             result.put("newBalance", getBalance(userId));
            
//         } catch (Exception e) {
//             result.put("status", "failed");
//             result.put("message", "Payment verification failed: " + e.getMessage());
//         }
        
//         return result;
//     }

//     /**
//      * ✅ Add referral bonus (called by ReferralService)
//      */
//     @Transactional
//     public void addReferralBonus(String userId, BigDecimal amount, String description) {
//         addMoneyWithTracking(
//             userId,
//             userId,
//             amount,
//             TransactionType.REFERRAL_REWARD,
//             PaymentGateway.NONE,
//             PaymentMethod.OTHER,
//             null,
//             description
//         );
//     }
// }
