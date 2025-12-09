package com.sensei.backend.controller;


import com.sensei.backend.entity.UserFeedback;
import com.sensei.backend.service.UserFeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class UserFeedbackController {

    private final UserFeedbackService userFeedbackService;


    // Create Feedback
    @PostMapping
    public ResponseEntity<UserFeedback> createFeedback(@RequestBody UserFeedback feedback) {
        UserFeedback savedFeedback = userFeedbackService.createFeedback(feedback);
        return new ResponseEntity<>(savedFeedback, HttpStatus.CREATED);
    }

    // Get All Feedback
    @GetMapping
    public ResponseEntity<List<UserFeedback>> getAllFeedback() {
        List<UserFeedback> feedbackList = userFeedbackService.getAllFeedback();
        return new ResponseEntity<>(feedbackList, HttpStatus.OK);
    }

    // Get Feedback by ID
    @GetMapping("/{id}")
    public ResponseEntity<UserFeedback> getFeedbackById(@PathVariable Long id) {
        UserFeedback feedback = userFeedbackService.getFeedbackById(id);
        return new ResponseEntity<>(feedback, HttpStatus.OK);
    }

    // Update Feedback
    @PutMapping("/{id}")
    public ResponseEntity<UserFeedback> updateFeedback(@PathVariable Long id, @RequestBody UserFeedback updatedFeedback) {
        UserFeedback feedback = userFeedbackService.updateFeedback(id, updatedFeedback);
        return new ResponseEntity<>(feedback, HttpStatus.OK);
    }

    // Delete Feedback
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFeedback(@PathVariable Long id) {
        userFeedbackService.deleteFeedback(id);
        return new ResponseEntity<>("Feedback deleted successfully", HttpStatus.OK);
    }
}