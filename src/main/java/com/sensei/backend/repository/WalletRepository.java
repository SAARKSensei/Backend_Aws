package com.sensei.backend.repository;

import com.sensei.backend.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, String> {
    
    // ✅ Add this line:
    Optional<Wallet> findByUserId(String userId);
}
