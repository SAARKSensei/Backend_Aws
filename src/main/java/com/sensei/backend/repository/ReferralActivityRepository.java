package com.sensei.backend.repository;

import com.sensei.backend.entity.ReferralActivity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReferralActivityRepository extends JpaRepository<ReferralActivity, String> {
	List<ReferralActivity> findByReferralCodeId(String referralCodeId);

    // ✅ Add this to fetch by userId
    List<ReferralActivity> findByUserId(String userId);
}
