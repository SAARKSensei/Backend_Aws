package com.sensei.backend.dto.interactiveprocesstracking;

import com.sensei.backend.enums.ProcessTrackingStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class InteractiveProcessTrackingResponseDTO {

    private UUID id;
    private UUID childId;
    private UUID interactiveProcessId;
    private ProcessTrackingStatus status;

    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
