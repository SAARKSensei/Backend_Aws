package com.sensei.backend.dto.parentquiz;

import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
public class SubmitQuizRequest {
    private UUID parentId;
    private UUID childId;
    private List<UUID> selectedOptionIds;
}
