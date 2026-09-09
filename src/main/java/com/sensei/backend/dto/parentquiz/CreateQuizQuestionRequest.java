package com.sensei.backend.dto.parentquiz;

import lombok.Data;
import java.util.List;

@Data
public class CreateQuizQuestionRequest {
    private String questionText;
    private Integer orderIndex;
    private List<CreateQuizOptionDto> options;
}
