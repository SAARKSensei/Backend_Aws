package com.sensei.backend.service;

import com.sensei.backend.dto.wallet.*;

import java.util.List;
import java.util.UUID;

public interface WalletService {

    // Create wallet if not exists (called on signup / first use)
    WalletResponseDTO getOrCreateWallet(UUID parentId);

    // Credit wallet (referral, cashback, wallet topup)
    WalletResponseDTO credit(WalletCreditRequestDTO dto);

    // Debit wallet (plan purchase)
    WalletResponseDTO debit(WalletDebitRequestDTO dto);

    // Get wallet details
    WalletResponseDTO getWallet(UUID parentId);

    // Get transaction history
    List<WalletTransactionResponseDTO> getTransactions(UUID parentId);
}
