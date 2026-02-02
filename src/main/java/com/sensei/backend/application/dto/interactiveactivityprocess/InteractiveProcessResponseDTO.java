package com.sensei.backend.application.dto.interactiveactivityprocess;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class InteractiveProcessResponseDTO {

    private UUID id;
    private UUID interactiveActivityId;
    private UUID subModuleId;
    private Integer stepOrder;
    private String senseiMessage;
    private String hint;
    private String childTask;
    private String mediaUrl;
    private String status;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
}
