package com.sensei.backend.repository;

import com.sensei.backend.entity.CouponUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponUsageRepository extends JpaRepository<CouponUsage, String> {
    boolean existsByUserIdAndCouponId(String userId, String couponId);
}
