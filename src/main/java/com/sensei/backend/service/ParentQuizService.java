package com.sensei.backend.service;

import com.sensei.backend.dto.parentquiz.ParentQuizResponse;
import com.sensei.backend.dto.parentquiz.SubmitQuizRequest;

import java.util.List;

public interface ParentQuizService {
    List<ParentQuizResponse> getQuizQuestions();
    void submitQuiz(SubmitQuizRequest request);
    void addQuizQuestions(java.util.List<com.sensei.backend.dto.parentquiz.CreateQuizQuestionRequest> requestList);
}
