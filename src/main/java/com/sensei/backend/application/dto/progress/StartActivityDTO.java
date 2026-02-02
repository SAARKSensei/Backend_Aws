package com.sensei.backend.application.dto.progress;

import lombok.Data;
import java.util.UUID;

@Data
public class StartActivityDTO {
    private UUID childId;
    private UUID interactiveActivityId;
}
