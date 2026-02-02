package com.sensei.backend.application.dto.pricingplansubject;

import lombok.Data;
import java.util.UUID;

@Data
public class PricingPlanSubjectRequestDTO {
    private UUID pricingPlanId;
    private UUID subjectId;
}
