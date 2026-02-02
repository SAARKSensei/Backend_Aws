package com.sensei.backend.application.dto.progress;

import lombok.Data;
import java.util.UUID;

@Data
public class QuestionAttemptDTO {
    private UUID childId;
    private UUID questionId;
    private UUID optionId;
}
