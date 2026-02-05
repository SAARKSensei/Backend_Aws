package com.sensei.backend.dto.question;

import com.sensei.backend.dto.questionOption.QuestionOptionResponseDTO;
import lombok.Data;
import java.util.List;
import java.util.UUID;

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
