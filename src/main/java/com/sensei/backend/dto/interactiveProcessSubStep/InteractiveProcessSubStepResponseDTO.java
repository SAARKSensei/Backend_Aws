package com.sensei.backend.dto.interactiveProcessSubStep;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class InteractiveProcessSubStepResponseDTO {

    private UUID id;
    private UUID processId;
    private Integer stepOrder;
    private String stepText;
    private String status;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
}
