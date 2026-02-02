package com.sensei.backend.application.dto.option;

import lombok.Data;
import java.util.UUID;

@Data
public class OptionRequestDTO {
    private UUID questionId;
    private String optionText;
    private String hint;
    private String counsellorNote;
    private Boolean isCorrect;
}
