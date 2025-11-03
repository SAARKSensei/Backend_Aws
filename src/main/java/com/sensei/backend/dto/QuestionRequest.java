package com.sensei.backend.dto;

import java.util.List;

public class QuestionRequest {

    private String questionText;
    private List<AnswerRequest> answers;

    // Constructor
    public QuestionRequest() {}

    // Getters and Setters
    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public List<AnswerRequest> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerRequest> answers) {
        this.answers = answers;
    }
}
