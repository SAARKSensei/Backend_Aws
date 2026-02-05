package com.sensei.backend.dto.processChildStep;

import lombok.Data;

import java.util.UUID;

@Data
public class ProcessChildStepRequestDTO {

    private UUID processId;
    private Integer stepOrder;
    private String stepText;
    private UUID activityId;
    private String activityType;
}
