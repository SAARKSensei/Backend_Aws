package com.sensei.backend.dto.interactiveprocess;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class InteractiveProcessResponseDTO {

    private UUID id;
    private UUID interactiveActivityId;
    private Integer stepOrder;
    private String senseiMessage;
    private String hint;
    private String childTask;
    private String mediaUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
