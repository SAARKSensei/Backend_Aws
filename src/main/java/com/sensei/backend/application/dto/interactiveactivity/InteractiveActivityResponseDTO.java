package com.sensei.backend.application.dto.interactiveactivity;

import lombok.Data;
import java.util.UUID;

@Data
public class InteractiveActivityResponseDTO {

    private UUID id;
    private UUID subModuleId;
    private String title;
    private String learningOutcome;
    private String keyObjectives;
    private String referenceVideo;
    private String coverImage;
    private String objective;
    private String activityType;
    private Integer orderIndex;
    private Boolean isActive;
}
