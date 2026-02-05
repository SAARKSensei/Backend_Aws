package com.sensei.backend.repository;

import com.sensei.backend.entity.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WalletTransactionRepository
        extends JpaRepository<WalletTransaction, UUID> {

    List<WalletTransaction> findByWalletIdOrderByCreatedAtDesc(UUID walletId);

    List<WalletTransaction> findByParentIdOrderByCreatedAtDesc(UUID parentId);

    List<WalletTransaction> findByReferenceTypeAndReferenceId(
            String referenceType,
            UUID referenceId
    );
}
