package com.sensei.backend.dto.pricingplansubjectbulkrequest;

import lombok.Data;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Data
public class PricingPlanSubjectBulkRequestDTO {

    @NotNull
    private UUID pricingPlanId;

    @NotEmpty
    private List<UUID> subjectIds;
}

