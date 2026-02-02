package com.sensei.backend.application.dto.processChildStep;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ProcessChildStepResponseDTO {

    private UUID id;
    private UUID processId;

    private Integer stepOrder;
    private String stepText;

    private UUID activityId;
    private String activityType;

    private String status;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
}
