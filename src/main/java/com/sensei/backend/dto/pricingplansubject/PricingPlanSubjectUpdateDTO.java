package com.sensei.backend.dto.pricingplansubject;

import lombok.Data;
import java.util.UUID;

@Data
public class PricingPlanSubjectUpdateDTO {
    private UUID pricingPlanId;
    private UUID subjectId;
}
