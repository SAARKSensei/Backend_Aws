package com.sensei.backend.repository;

import com.sensei.backend.entity.PricingPlanSubject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PricingPlanSubjectRepository extends JpaRepository<PricingPlanSubject, UUID> {

    // ✅ Access control check
    boolean existsByPricingPlan_IdAndSubject_Id(UUID pricingPlanId, UUID subjectId);

    // ✅ Used in PricingPlanSubjectServiceImpl
    List<PricingPlanSubject> findByPricingPlan_Id(UUID pricingPlanId);

    List<PricingPlanSubject> findBySubject_Id(UUID subjectId);
}
