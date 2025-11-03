package com.sensei.backend.service;

import com.sensei.backend.entity.ReferralCode;
import com.sensei.backend.entity.ReferralActivity;
import com.sensei.backend.entity.Wallet;
import com.sensei.backend.entity.WalletTransaction;
import com.sensei.backend.repository.ReferralCodeRepository;
import com.sensei.backend.repository.ReferralActivityRepository;
import com.sensei.backend.repository.WalletRepository;
import com.sensei.backend.repository.WalletTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class ReferralService {

    @Autowired
    private ReferralCodeRepository referralCodeRepository;

    @Autowired
    private ReferralActivityRepository referralActivityRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private WalletTransactionRepository walletTransactionRepository;

    // ✅ Create referral (no bonus amount here)
    public ReferralCode createReferral(String referrerUserId, String referredUserId, String code, Boolean isSchool) {
        ReferralCode referral = new ReferralCode();
        referral.setReferrerUserId(referrerUserId);
        referral.setReferredUserId(referredUserId);
        referral.setCode(code);
        referral.setIsSchool(isSchool);
        return referralCodeRepository.save(referral);
    }

    // ✅ Apply referral and give wallet bonus to both users
    public String applyReferral(String code, String referredUserId) {
        ReferralCode referral = referralCodeRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Referral code not found!"));

        // Example bonus distribution logic
        BigDecimal referrerBonus = BigDecimal.valueOf(100);
        BigDecimal referredBonus = BigDecimal.valueOf(150);

        // 1️⃣ Add to Referrer Wallet
        Wallet referrerWallet = walletRepository.findByUserId(referral.getReferrerUserId())
                .orElseGet(() -> {
                    Wallet newWallet = new Wallet();
                    newWallet.setUserId(referral.getReferrerUserId());
                    newWallet.setBalance(BigDecimal.ZERO);
                    return walletRepository.save(newWallet);
                });

        referrerWallet.setBalance(referrerWallet.getBalance().add(referrerBonus));
        walletRepository.save(referrerWallet);

        // Record in wallet_transactions for referrer
        WalletTransaction referrerTransaction = new WalletTransaction();
        referrerTransaction.setWallet(referrerWallet);
        referrerTransaction.setUserId(referral.getReferrerUserId());
        referrerTransaction.setAmount(referrerBonus);
        referrerTransaction.setTransactionType("REFERRAL_BONUS_REFERRER");
        referrerTransaction.setDescription("Referral bonus credited to referrer");
        referrerTransaction.setCreatedAt(LocalDateTime.now());
        walletTransactionRepository.save(referrerTransaction);

        // 2️⃣ Add to Referred Wallet
        Wallet referredWallet = walletRepository.findByUserId(referredUserId)
                .orElseGet(() -> {
                    Wallet newWallet = new Wallet();
                    newWallet.setUserId(referredUserId);
                    newWallet.setBalance(BigDecimal.ZERO);
                    return walletRepository.save(newWallet);
                });

        referredWallet.setBalance(referredWallet.getBalance().add(referredBonus));
        walletRepository.save(referredWallet);

        // Record in wallet_transactions for referred user
        WalletTransaction referredTransaction = new WalletTransaction();
        referredTransaction.setWallet(referredWallet);
        referredTransaction.setUserId(referredUserId);
        referredTransaction.setAmount(referredBonus);
        referredTransaction.setTransactionType("REFERRAL_BONUS_REFERRED");
        referredTransaction.setDescription("Referral bonus credited to referred user");
        referredTransaction.setCreatedAt(LocalDateTime.now());
        walletTransactionRepository.save(referredTransaction);

        // Save referral activity
        ReferralActivity activity = new ReferralActivity();
        activity.setReferralCodeId(referral.getId());
        activity.setUserId(referredUserId);
        referralActivityRepository.save(activity);

        return String.format(
                "Referral applied successfully! Referrer (%s) credited ₹%.2f, Referred (%s) credited ₹%.2f",
                referral.getReferrerUserId(), referrerBonus, referredUserId, referredBonus
        );
    }
}
