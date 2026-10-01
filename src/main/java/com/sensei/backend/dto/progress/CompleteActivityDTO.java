package com.sensei.backend.dto.progress;

import lombok.Data;
import java.util.UUID;

@Data
public class CompleteActivityDTO {
    private UUID childId;
    private UUID interactiveActivityId;
    private Long timeTakenSeconds;
    private Integer feedbackStars;
    private String feedbackMessage;
}
