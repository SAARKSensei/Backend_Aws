package com.sensei.backend.application.dto.questionOption;

import lombok.Data;
import java.util.UUID;

@Data
public class QuestionOptionRequestDTO {
    private UUID questionId;
    private String optionText;
    private Boolean isCorrect;
    private String hint;
    private String counsellorNote;
    private Integer orderIndex;
}
