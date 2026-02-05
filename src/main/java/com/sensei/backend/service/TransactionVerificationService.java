// package com.sensei.backend.service;

// import com.sensei.backend.entity.Wallet;
// import com.sensei.backend.entity.WalletTransaction;
// import com.sensei.backend.entity.TransactionMaster;
// import com.sensei.backend.enums.TransactionStatus;
// import com.sensei.backend.repository.WalletRepository;
// import com.sensei.backend.repository.WalletTransactionRepository;
// import com.sensei.backend.repository.TransactionMasterRepository;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import java.math.BigDecimal;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;

// @Service
// public class TransactionVerificationService {

//     @Autowired
//     private WalletRepository walletRepository;

//     @Autowired
//     private WalletTransactionRepository walletTransactionRepository;

//     @Autowired
//     private TransactionMasterRepository transactionMasterRepository;

//     /**
//      * Verify wallet balance matches transaction history
//      */
//     public Map<String, Object> verifyWalletConsistency(String userId) {
//         Map<String, Object> result = new HashMap<>();
        
//         // 1. Get balance from wallet table
//         BigDecimal walletBalance = walletRepository.findByUserId(userId)
//             .map(Wallet::getBalance)
//             .orElse(BigDecimal.ZERO);
        
//         // 2. Calculate balance from wallet_transactions
//         List<WalletTransaction> walletTxns = walletTransactionRepository
//             .findByUserIdOrderByCreatedAtDesc(userId);
        
//         BigDecimal calculatedFromWalletTxns = walletTxns.stream()
//             .map(WalletTransaction::getAmount)
//             .reduce(BigDecimal.ZERO, BigDecimal::add);
        
//         // 3. Calculate balance from transaction_master
//         List<TransactionMaster> masterTxns = transactionMasterRepository
//             .findByChildIdAndStatus(userId, TransactionStatus.SUCCESS);
        
//         BigDecimal calculatedFromMaster = masterTxns.stream()
//             .map(t -> {
//                 if (t.getWalletCredit()) {
//                     return t.getFinalAmount();
//                 } else {
//                     return t.getFinalAmount().negate();
//                 }
//             })
//             .reduce(BigDecimal.ZERO, BigDecimal::add);
        
//         // 4. Compare
//         boolean consistent = walletBalance.compareTo(calculatedFromWalletTxns) == 0 &&
//                             walletBalance.compareTo(calculatedFromMaster) == 0;
        
//         result.put("userId", userId);
//         result.put("walletTableBalance", walletBalance);
//         result.put("calculatedFromWalletTransactions", calculatedFromWalletTxns);
//         result.put("calculatedFromTransactionMaster", calculatedFromMaster);
//         result.put("consistent", consistent);
//         result.put("walletTransactionsCount", walletTxns.size());
//         result.put("transactionMasterCount", masterTxns.size());
        
//         if (!consistent) {
//             result.put("warning", "⚠️ Inconsistency detected! Balances don't match.");
//         } else {
//             result.put("message", "✅ All balances match perfectly!");
//         }
        
//         return result;
//     }

//     /**
//      * Get transaction comparison report
//      */
//     public Map<String, Object> getTransactionComparison(String userId) {
//         Map<String, Object> result = new HashMap<>();
        
//         List<WalletTransaction> walletTxns = walletTransactionRepository
//             .findByUserIdOrderByCreatedAtDesc(userId);
        
//         List<TransactionMaster> masterTxns = transactionMasterRepository
//             .findByChildIdOrderByTransactionDateDesc(userId);
        
//         result.put("userId", userId);
//         result.put("walletTransactions", walletTxns);
//         result.put("transactionMaster", masterTxns);
        
//         return result;
//     }
// }
