package com.sensei.backend.repository;

import com.sensei.backend.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface WalletRepository extends JpaRepository<Wallet, UUID> {

    Optional<Wallet> findByParentId(UUID parentId);

    boolean existsByParentId(UUID parentId);
}
