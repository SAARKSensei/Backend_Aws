package com.sensei.backend.repository;

import com.sensei.backend.entity.ReferralCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ReferralCodeRepository extends JpaRepository<ReferralCode, String> {
    
    Optional<ReferralCode> findByCode(String code);
    
    Optional<ReferralCode> findByReferrerUserId(String referrerUserId);
    
    boolean existsByCode(String code);
}
