package com.sensei.backend.repository;

import com.sensei.backend.entity.CouponUsage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CouponUsageRepository extends JpaRepository<CouponUsage, UUID> {

    boolean existsByCouponIdAndParentId(UUID couponId, UUID parentId);
}
