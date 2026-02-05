package com.sensei.backend.dto.interactiveProcessSubStep;

import lombok.Data;
import java.util.UUID;

@Data
public class InteractiveProcessSubStepRequestDTO {

    private UUID processId;
    private Integer stepOrder;
    private String stepText;
}
