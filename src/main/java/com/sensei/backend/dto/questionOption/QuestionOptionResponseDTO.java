package com.sensei.backend.dto.questionOption;

import lombok.Data;
import java.util.UUID;

@Data
public class QuestionOptionResponseDTO {

    private UUID id;
    private UUID questionId;
    private String optionText;
    private Boolean correct;
    private String hint;
    private String counsellorNote;
    private Integer orderIndex;
}
