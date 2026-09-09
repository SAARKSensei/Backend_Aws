package com.sensei.backend.controller;

import com.sensei.backend.dto.parentquiz.CreateQuizQuestionRequest;
import com.sensei.backend.dto.parentquiz.ParentQuizResponse;
import com.sensei.backend.dto.parentquiz.SubmitQuizRequest;
import com.sensei.backend.service.ParentQuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/parent-quiz")
@RequiredArgsConstructor
public class ParentQuizController {

    private final ParentQuizService parentQuizService;

    @GetMapping
    public ResponseEntity<List<ParentQuizResponse>> getQuizQuestions() {
        return ResponseEntity.ok(parentQuizService.getQuizQuestions());
    }

    @PostMapping("/submit")
    public ResponseEntity<Void> submitQuiz(@RequestBody SubmitQuizRequest request) {
        parentQuizService.submitQuiz(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<String> addQuizQuestions(@RequestBody List<CreateQuizQuestionRequest> requestList) {
        parentQuizService.addQuizQuestions(requestList);
        return ResponseEntity.ok("Quiz questions added successfully!");
    }
}
