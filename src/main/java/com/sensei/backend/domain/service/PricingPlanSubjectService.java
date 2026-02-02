package com.sensei.backend.domain.service;

import java.util.List;
import java.util.UUID;

import com.sensei.backend.application.dto.pricingplansubject.*;

public interface PricingPlanSubjectService {
    PricingPlanSubjectResponseDTO add(PricingPlanSubjectRequestDTO dto);
    void remove(UUID id);
    List<PricingPlanSubjectResponseDTO> getByPlan(UUID planId);
    List<PricingPlanSubjectResponseDTO> getBySubject(UUID subjectId);
}
