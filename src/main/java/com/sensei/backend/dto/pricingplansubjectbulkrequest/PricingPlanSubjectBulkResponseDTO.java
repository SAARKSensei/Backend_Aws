package com.sensei.backend.dto.pricingplansubjectbulkrequest;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class PricingPlanSubjectBulkResponseDTO {

    private UUID pricingPlanId;
    private String pricingPlanName;
    private Integer totalSubjectsAttached;

    private List<SubjectSummaryDTO> subjects;

    @Data
    @Builder
    public static class SubjectSummaryDTO {
        private UUID subjectId;
        private String subjectName;
    }
}
