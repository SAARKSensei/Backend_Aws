package com.sensei.backend.application.dto.pricingplansubject;

import lombok.Data;
import java.util.UUID;

@Data
public class PricingPlanSubjectResponseDTO {
    private UUID id;
    private UUID pricingPlanId;
    private UUID subjectId;
}
