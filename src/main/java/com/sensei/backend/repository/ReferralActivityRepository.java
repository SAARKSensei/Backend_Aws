package com.sensei.backend.repository;

import com.sensei.backend.entity.ReferralActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReferralActivityRepository extends JpaRepository<ReferralActivity, String> {
    
    boolean existsByReferredUserId(String referredUserId);
    
    List<ReferralActivity> findByReferralCodeId(String referralCodeId);
}
