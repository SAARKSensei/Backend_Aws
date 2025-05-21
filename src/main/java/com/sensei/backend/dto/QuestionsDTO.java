package com.sensei.backend.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class QuestionsDTO {
    private String questionId;

    private int questionNumber;

    private String questionName;

    private String senseiQuestion;

    private String option1;

    private String option2;

    private String option3;

    private String senseiAnswer;

    private String correctAnswerDescription;

    private String incorrectAnswerDescription;

    private String questionImage;

    private int digitalActivityRef;

    // New Field
    private String digitalActivityIdRef;

    // New Field
    private String nextQuestionIdRef;
}
