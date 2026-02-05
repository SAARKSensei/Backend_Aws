package com.sensei.backend.dto.pricingplansubject;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class PricingPlanSubjectResponseDTO {

    private UUID mappingId;

    private UUID pricingPlanId;
    private String pricingPlanName;

    private UUID subjectId;
    private String subjectName;
}

