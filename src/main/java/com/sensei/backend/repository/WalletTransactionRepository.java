package com.sensei.backend.repository;

import com.sensei.backend.entity.WalletTransaction;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, String> {

	List<WalletTransaction> findByUserIdAndTransactionType(String userId, String transactionType);

	List<WalletTransaction> findByUserId(String userId);


}
