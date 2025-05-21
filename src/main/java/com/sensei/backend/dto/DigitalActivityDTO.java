package com.sensei.backend.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class DigitalActivityDTO {
    private String digitalActivityId;

    @NotBlank
    private String digitalActivityName;

    private String keyOutcomes;

    @NotBlank
    private String numberOfQuestion;

    private String image;

    private String ratings;

    private String feedback;

    private String tags;

    private float progress;

    private List<QuestionsDTO> questions;

    // New Field
    private String submoduleIdRef;

    // New Field
    private String firstQuestionIdRef;
}
