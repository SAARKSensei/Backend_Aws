package com.sensei.backend.controller;
import com.sensei.backend.dto.pricingplansubject.PricingPlanSubjectResponseDTO;
import com.sensei.backend.dto.pricingplansubject.PricingPlanSubjectUpdateDTO;
import com.sensei.backend.dto.pricingplansubjectbulkrequest.PricingPlanSubjectBulkRequestDTO;
import com.sensei.backend.dto.pricingplansubjectbulkrequest.PricingPlanSubjectBulkResponseDTO;
import com.sensei.backend.service.PricingPlanSubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


import java.util.UUID;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/pricing-plan-subjects")
@RequiredArgsConstructor
public class PricingPlanSubjectController {

    private final PricingPlanSubjectService service;

    // 1️⃣ Attach multiple subjects
    @PostMapping("/attach-multiple")
    public ResponseEntity<PricingPlanSubjectBulkResponseDTO> attachMultiple(
            @RequestBody @Valid PricingPlanSubjectBulkRequestDTO dto) {
        return ResponseEntity.ok(service.attachMultipleSubjects(dto));
    }

    // 2️⃣ Get ALL mappings
    @GetMapping
    public ResponseEntity<List<PricingPlanSubjectResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // 3️⃣ Get by PRICING PLAN
    @GetMapping("/by-plan/{planId}")
    public ResponseEntity<List<PricingPlanSubjectResponseDTO>> getByPlan(
            @PathVariable UUID planId) {
        return ResponseEntity.ok(service.getByPricingPlan(planId));
    }

    // 4️⃣ ✅ GET BY SUBJECT ID (THIS ANSWERS YOUR QUESTION)
    @GetMapping("/by-subject/{subjectId}")
    public ResponseEntity<List<PricingPlanSubjectResponseDTO>> getBySubject(
            @PathVariable UUID subjectId) {
        return ResponseEntity.ok(service.getBySubject(subjectId));
    }

    // 5️⃣ Update mapping
    @PutMapping("/{mappingId}")
    public ResponseEntity<PricingPlanSubjectResponseDTO> update(
            @PathVariable UUID mappingId,
            @RequestBody PricingPlanSubjectUpdateDTO dto) {
        return ResponseEntity.ok(service.update(mappingId, dto));
    }

    // 6️⃣ Delete mapping
    @DeleteMapping("/{mappingId}")
    public ResponseEntity<Void> delete(@PathVariable UUID mappingId) {
        service.delete(mappingId);
        return ResponseEntity.noContent().build();
    }
}
