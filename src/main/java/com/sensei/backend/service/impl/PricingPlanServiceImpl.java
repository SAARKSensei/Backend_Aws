package com.sensei.backend.service.impl;

import com.sensei.backend.entity.PricingPlan;
import com.sensei.backend.repository.PricingPlanRepository;
import com.sensei.backend.service.PricingPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PricingPlanServiceImpl implements PricingPlanService {

    private final PricingPlanRepository pricingPlanRepository;

    @Override
    @Transactional
    public PricingPlan create(PricingPlan plan) {
        return pricingPlanRepository.save(plan);
    }

    @Override
    public PricingPlan getById(UUID id) {
        return pricingPlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pricing Plan not found"));
    }

    @Override
    public List<PricingPlan> getAll() {
        return pricingPlanRepository.findAll();
    }

    @Override
    @Transactional
    public PricingPlan update(UUID id, PricingPlan updatedPlan) {
        PricingPlan plan = getById(id);

        plan.setName(updatedPlan.getName());
        plan.setPrice(updatedPlan.getPrice());
        plan.setDurationMonths(updatedPlan.getDurationMonths());
        plan.setGrade(updatedPlan.getGrade());
        plan.setStatus(updatedPlan.getStatus());
        plan.setDescription(updatedPlan.getDescription());

        return pricingPlanRepository.save(plan);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        pricingPlanRepository.deleteById(id);
    }
}
