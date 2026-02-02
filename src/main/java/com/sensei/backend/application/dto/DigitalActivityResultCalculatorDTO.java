package com.sensei.backend.application.dto;

import lombok.Data;
import java.util.List;

@Data
public class DigitalActivityResultCalculatorDTO {
    private String childId;
    private String digitalActivityId;
    private List<QuestionAttempt> questions;

    @Data
    public static class QuestionAttempt {
        private String questionId;
        private int attemptCount;
        private boolean correct;
    }
}
