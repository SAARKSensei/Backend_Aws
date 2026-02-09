package com.sensei.backend.repository;

import com.sensei.backend.entity.MasterTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MasterTransactionRepository
        extends JpaRepository<MasterTransaction, UUID> {

    List<MasterTransaction> findByParentIdOrderByCreatedAtDesc(UUID parentId);

    List<MasterTransaction> findByChildIdOrderByCreatedAtDesc(UUID childId);

    List<MasterTransaction> findByTransactionType(String transactionType);

    boolean existsByPaymentTransactionId(UUID paymentTransactionId);

}
