package com.sensei.backend.repository;

import com.sensei.backend.entity.PricingPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PricingPlanRepository extends JpaRepository<PricingPlan, String> {
    Optional<PricingPlan> findById(String id);     // Added by Vaishnav Kale
}
