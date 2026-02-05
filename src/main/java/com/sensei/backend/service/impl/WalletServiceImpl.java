package com.sensei.backend.service.impl;

import com.sensei.backend.dto.wallet.*;
import com.sensei.backend.entity.Wallet;
import com.sensei.backend.entity.WalletTransaction;
import com.sensei.backend.repository.WalletRepository;
import com.sensei.backend.repository.WalletTransactionRepository;
import com.sensei.backend.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final WalletTransactionRepository walletTransactionRepository;

    // ----------------------------------------
    // GET OR CREATE WALLET
    // ----------------------------------------
    @Override
    @Transactional
    public WalletResponseDTO getOrCreateWallet(UUID parentId) {

        Wallet wallet = walletRepository
                .findByParentId(parentId)
                .orElseGet(() -> {
                    Wallet w = Wallet.builder()
                            .parentId(parentId)
                            .balance(0)
                            .status("ACTIVE")
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build();
                    return walletRepository.save(w);
                });

        return mapWallet(wallet);
    }

    // ----------------------------------------
    // CREDIT WALLET
    // ----------------------------------------
    @Override
    @Transactional
    public WalletResponseDTO credit(WalletCreditRequestDTO dto) {

        Wallet wallet = getWalletEntity(dto.getParentId());

        wallet.setBalance(wallet.getBalance() + dto.getAmount());
        wallet.setUpdatedAt(LocalDateTime.now());
        walletRepository.save(wallet);

        WalletTransaction tx = WalletTransaction.builder()
                .walletId(wallet.getId())
                .parentId(dto.getParentId())
                .amount(dto.getAmount())
                .direction("CREDIT")
                .transactionType(dto.getTransactionType())
                .referenceType(dto.getReferenceType())
                .referenceId(dto.getReferenceId())
                .remarks(dto.getRemarks())
                .createdAt(LocalDateTime.now())
                .build();

        walletTransactionRepository.save(tx);

        return mapWallet(wallet);
    }

    // ----------------------------------------
    // DEBIT WALLET
    // ----------------------------------------
    @Override
    @Transactional
    public WalletResponseDTO debit(WalletDebitRequestDTO dto) {

        Wallet wallet = getWalletEntity(dto.getParentId());

        if (wallet.getBalance() < dto.getAmount()) {
            throw new RuntimeException("Insufficient wallet balance");
        }

        wallet.setBalance(wallet.getBalance() - dto.getAmount());
        wallet.setUpdatedAt(LocalDateTime.now());
        walletRepository.save(wallet);

        WalletTransaction tx = WalletTransaction.builder()
                .walletId(wallet.getId())
                .parentId(dto.getParentId())
                .amount(dto.getAmount())
                .direction("DEBIT")
                .transactionType(dto.getTransactionType())
                .referenceType("PRICING_PLAN")
                .referenceId(dto.getPricingPlanId())
                .remarks(dto.getRemarks())
                .createdAt(LocalDateTime.now())
                .build();

        walletTransactionRepository.save(tx);

        return mapWallet(wallet);
    }

    // ----------------------------------------
    // GET WALLET
    // ----------------------------------------
    @Override
    public WalletResponseDTO getWallet(UUID parentId) {
        return mapWallet(getWalletEntity(parentId));
    }

    // ----------------------------------------
    // WALLET TRANSACTIONS
    // ----------------------------------------
    @Override
    public List<WalletTransactionResponseDTO> getTransactions(UUID parentId) {

        return walletTransactionRepository
                .findByParentIdOrderByCreatedAtDesc(parentId)
                .stream()
                .map(tx -> WalletTransactionResponseDTO.builder()
                        .id(tx.getId())
                        .amount(tx.getAmount())
                        .direction(tx.getDirection())
                        .transactionType(tx.getTransactionType())
                        .referenceType(tx.getReferenceType())
                        .referenceId(tx.getReferenceId())
                        .remarks(tx.getRemarks())
                        .createdAt(tx.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    // ----------------------------------------
    // INTERNAL HELPERS
    // ----------------------------------------
    private Wallet getWalletEntity(UUID parentId) {
        return walletRepository
                .findByParentId(parentId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));
    }

    private WalletResponseDTO mapWallet(Wallet wallet) {
        return WalletResponseDTO.builder()
                .walletId(wallet.getId())
                .parentId(wallet.getParentId())
                .balance(wallet.getBalance())
                .status(wallet.getStatus())
                .build();
    }
}
