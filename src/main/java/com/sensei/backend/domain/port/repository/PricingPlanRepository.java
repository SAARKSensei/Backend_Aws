// package com.sensei.backend.repository;

// import com.sensei.backend.entity.PricingPlan;
// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.stereotype.Repository;

// import java.util.Optional;

// @Repository
// public interface PricingPlanRepository extends JpaRepository<PricingPlan, String> {
//     Optional<PricingPlan> findById(String id);     // Added by Vaishnav Kale
// }
package com.sensei.backend.domain.port.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sensei.backend.adapter.persistence.jpa.entity.PricingPlan;

import java.util.List;
import java.util.UUID;

public interface PricingPlanRepository
        extends JpaRepository<PricingPlan, UUID> {

    List<PricingPlan> findByStatus(String status);
}
