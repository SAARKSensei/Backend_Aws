package com.sensei.backend.controller;

import com.sensei.backend.dto.QuestionsDTO;
import com.sensei.backend.service.QuestionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/questions")
public class QuestionsController {

    @Autowired
    private QuestionsService questionsService;

    @PostMapping
    public ResponseEntity<QuestionsDTO> createQuestions(@Valid @RequestBody QuestionsDTO questionsDTO) {
        QuestionsDTO createdQuestions = questionsService.createQuestions(questionsDTO);
        return new ResponseEntity<>(createdQuestions, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<QuestionsDTO>> getAllQuestions() {
        List<QuestionsDTO> questions = questionsService.getAllQuestions();
        return new ResponseEntity<>(questions, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionsDTO> getQuestionsById(@PathVariable String id) {
        Optional<QuestionsDTO> questions = questionsService.getQuestionsById(id);
        return questions.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuestionsDTO> updateQuestions(@PathVariable String id, @Valid @RequestBody QuestionsDTO questionsDTO) {
        QuestionsDTO updatedQuestions = questionsService.updateQuestions(id, questionsDTO);
        return new ResponseEntity<>(updatedQuestions, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestions(@PathVariable String id) {
        questionsService.deleteQuestions(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
