package com.sensei.backend.dto.question;

import lombok.Data;
import java.util.UUID;

@Data
public class QuestionRequestDTO {
    private UUID digitalActivityId;
    private String questionText;
    private String hint;
    private String counsellorNote;
    private String explanation;
    private Integer orderIndex;
}
