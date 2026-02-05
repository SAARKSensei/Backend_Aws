package com.sensei.backend.controller;

import com.sensei.backend.dto.progress.*;
import com.sensei.backend.service.ChildProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/progress")
@RequiredArgsConstructor
public class ChildProgressController {

    private final ChildProgressService childProgressService;

    // Child starts an Interactive Activity
    @PostMapping("/activity/start")
    public ResponseEntity<String> startActivity(@RequestBody StartActivityDTO dto) {
        childProgressService.startInteractiveActivity(dto);
        return ResponseEntity.ok("Activity started");
    }

    // Child completes an Interactive Activity
    @PostMapping("/activity/complete")
    public ResponseEntity<String> completeActivity(@RequestBody CompleteActivityDTO dto) {
        childProgressService.completeInteractiveActivity(dto);
        return ResponseEntity.ok("Activity completed");
    }

    // Child answers a Question
    @PostMapping("/question/attempt")
    public ResponseEntity<String> attempt(@RequestBody QuestionAttemptDTO dto) {
        childProgressService.recordQuestionAttempt(dto);
        return ResponseEntity.ok("Answer recorded");
    }
}
