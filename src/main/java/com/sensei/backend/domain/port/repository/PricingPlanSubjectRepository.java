package com.sensei.backend.domain.port.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sensei.backend.adapter.persistence.jpa.entity.PricingPlanSubject;

import java.util.List;
import java.util.UUID;

public interface PricingPlanSubjectRepository
        extends JpaRepository<PricingPlanSubject, UUID> {

    List<PricingPlanSubject> findByPricingPlanId(UUID planId);

    List<PricingPlanSubject> findBySubjectId(UUID subjectId);
}
