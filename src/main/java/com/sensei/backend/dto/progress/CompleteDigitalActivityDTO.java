package com.sensei.backend.dto.progress;

import lombok.Data;
import java.util.UUID;
import java.util.List;

@Data
public class CompleteDigitalActivityDTO {
    private UUID childId;
    private UUID digitalActivityId;
    private Long timeTakenSeconds;
    private Integer feedbackStars;
    private String feedbackMessage;
    private List<QuestionAttemptDTO> attempts;
}
