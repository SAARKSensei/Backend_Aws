package com.sensei.backend.dto.progress;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HierarchicalProgressDTO {
    private Integer completedCount;
    private Integer totalCount;
    private Boolean isCompleted;
    private String status;
}
