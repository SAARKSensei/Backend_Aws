package com.sensei.backend.application.dto.process;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class ProcessResponseDTO {
    private UUID id;
    private UUID interactiveActivityId;
    private String senseiMessage;
    private String hint;
    private List<String> childSteps;
    private String mediaUrl;
    private Integer stepOrder;
    private LocalDateTime createdAt;
}

