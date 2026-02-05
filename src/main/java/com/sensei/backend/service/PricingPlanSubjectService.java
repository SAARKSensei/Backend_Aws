package com.sensei.backend.service;

import com.sensei.backend.dto.pricingplansubject.PricingPlanSubjectResponseDTO;
import com.sensei.backend.dto.pricingplansubject.PricingPlanSubjectUpdateDTO;
import com.sensei.backend.dto.pricingplansubjectbulkrequest.PricingPlanSubjectBulkRequestDTO;
import com.sensei.backend.dto.pricingplansubjectbulkrequest.PricingPlanSubjectBulkResponseDTO;

import java.util.List;
import java.util.UUID;

public interface PricingPlanSubjectService {

    // BULK ATTACH
    PricingPlanSubjectBulkResponseDTO attachMultipleSubjects(
            PricingPlanSubjectBulkRequestDTO dto
    );

    // READ
    List<PricingPlanSubjectResponseDTO> getAll();
    List<PricingPlanSubjectResponseDTO> getByPricingPlan(UUID pricingPlanId);
    List<PricingPlanSubjectResponseDTO> getBySubject(UUID subjectId);

    // UPDATE
    PricingPlanSubjectResponseDTO update(
            UUID mappingId,
            PricingPlanSubjectUpdateDTO dto
    );

    // DELETE
    void delete(UUID mappingId);
}
