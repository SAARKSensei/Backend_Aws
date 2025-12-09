package com.sensei.backend.service;

import com.sensei.backend.entity.Coupon;
import com.sensei.backend.entity.User;
import com.sensei.backend.entity.Wallet;
import com.sensei.backend.entity.WalletTransaction;
import com.sensei.backend.repository.CouponRepository;
import com.sensei.backend.repository.UserRepository;
import com.sensei.backend.repository.WalletRepository;
import com.sensei.backend.repository.WalletTransactionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private WalletTransactionRepository walletTransactionRepository;

    // ✅ Create new coupon
    public Coupon createCoupon(Coupon coupon) {
        if (coupon.getUser() == null || coupon.getUser().getUserId() == null) {
            throw new RuntimeException("User ID is required to create a coupon");
        }

        // ✅ Fetch existing user from DB
        User existingUser = userRepository.findById(coupon.getUser().getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + coupon.getUser().getUserId()));

        // ✅ Set the fetched user to coupon and save
        coupon.setUser(existingUser);
        coupon.setActive(true);
        coupon.setCreatedAt(LocalDateTime.now());

        return couponRepository.save(coupon);
    }

    // ✅ Apply coupon to wallet
    public String applyCoupon(String userId, String couponCode) {
        Coupon coupon = couponRepository.findByCode(couponCode)
                .orElseThrow(() -> new RuntimeException("Coupon not found with code: " + couponCode));

        if (!coupon.isActive()) {
            throw new RuntimeException("Coupon is inactive or already used");
        }

        if (coupon.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Coupon has expired");
        }

        // ✅ Fetch user's wallet
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Wallet not found for user: " + userId));

        // ✅ Add coupon amount to wallet
        BigDecimal amount = coupon.getDiscountAmount();
        wallet.setBalance(wallet.getBalance().add(amount));
        walletRepository.save(wallet);

        // ✅ Record the wallet transaction
        WalletTransaction txn = new WalletTransaction();
        txn.setWallet(wallet);
        txn.setAmount(amount);
        txn.setUserId(wallet.getUserId()); 
        txn.setTransactionType("COUPON_CREDIT");
        txn.setDescription("Coupon applied: " + couponCode);
        txn.setCreatedAt(LocalDateTime.now());
        walletTransactionRepository.save(txn);

        // ✅ Mark coupon as used
        coupon.setActive(false);
        couponRepository.save(coupon);

        return "✅ Coupon applied successfully! ₹" + amount + " added to your wallet.";
    }
}
