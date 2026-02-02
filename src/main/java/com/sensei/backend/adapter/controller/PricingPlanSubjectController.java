package com.sensei.backend.adapter.controller;

import com.sensei.backend.application.dto.pricingplansubject.*;
import com.sensei.backend.domain.service.PricingPlanSubjectService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/pricing-plan-subjects")
@RequiredArgsConstructor
public class PricingPlanSubjectController {

    private final PricingPlanSubjectService service;

    @PostMapping
    public PricingPlanSubjectResponseDTO add(@RequestBody PricingPlanSubjectRequestDTO dto) {
        return service.add(dto);
    }

    @GetMapping("/plan/{planId}")
    public List<PricingPlanSubjectResponseDTO> getByPlan(@PathVariable UUID planId) {
        return service.getByPlan(planId);
    }

    @GetMapping("/subject/{subjectId}")
    public List<PricingPlanSubjectResponseDTO> getBySubject(@PathVariable UUID subjectId) {
        return service.getBySubject(subjectId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        service.remove(id);
    }
}
