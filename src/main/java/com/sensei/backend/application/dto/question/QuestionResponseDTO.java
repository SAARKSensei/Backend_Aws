package com.sensei.backend.application.dto.question;

import lombok.Data;
import java.util.List;
import java.util.UUID;

import com.sensei.backend.application.dto.questionOption.QuestionOptionResponseDTO;

@Data
public class QuestionResponseDTO {
    private UUID id;
    private UUID digitalActivityId;
    private String questionText;
    private String hint;
    private String counsellorNote;
    private String explanation;
    private Integer orderIndex;
    private List<QuestionOptionResponseDTO> options;
}
