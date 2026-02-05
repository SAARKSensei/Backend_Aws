package com.sensei.backend.dto.pricingplansubjectbulkrequest;

import lombok.Data;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Data
public class PricingPlanSubjectBulkRequestDTO {

    @NotNull
    private UUID pricingPlanId;

    @NotEmpty
    private List<UUID> subjectIds;
}
