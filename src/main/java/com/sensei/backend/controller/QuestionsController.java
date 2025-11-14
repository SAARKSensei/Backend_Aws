package com.sensei.backend.controller;

import com.sensei.backend.entity.Question;
import com.sensei.backend.dto.QuestionsDTO;
import com.sensei.backend.service.QuestionsService;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
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

    // ✅ Create Question linked to a specific DigitalActivity
    @PostMapping
    public ResponseEntity<Question> createQuestion(@Valid @RequestBody Question question) {
        Question saved = questionsService.createQuestion(question, question.getDigitalActivityIdRef());
        return ResponseEntity.status(201).body(saved);
    }

    // ✅ Create from DTO (optional)
    @PostMapping("/dto")
    public ResponseEntity<QuestionsDTO> createQuestionFromDTO(
            @Valid @RequestBody QuestionsDTO dto,
            @RequestParam String digitalActivityId) {

        QuestionsDTO savedDto = questionsService.createQuestionFromDTO(dto, digitalActivityId);
        return ResponseEntity.status(201).body(savedDto);
    }

    @GetMapping
    public ResponseEntity<List<QuestionsDTO>> getAllQuestions() {
        List<QuestionsDTO> dtos = questionsService.getAllQuestionsDTO();
        return ResponseEntity.ok().body(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getQuestionById(@PathVariable String id) {
        try {
            Optional<Question> opt = questionsService.findById(id);
            if (opt.isPresent()) {
                return ResponseEntity.ok(opt.get());
            } else {
                return ResponseEntity.status(HttpStatus.SC_NOT_FOUND)
                        .body("Question not found with id: " + id);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                    .body("Error fetching Question: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateQuestion(@PathVariable String id, @Valid @RequestBody Question updated) {
        try {
            Question saved = questionsService.updateQuestion(id, updated);
            return ResponseEntity.ok(saved);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND)
                    .body("Update failed: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                    .body("Error updating Question: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteQuestion(@PathVariable String id) {
        try {
            questionsService.deleteQuestion(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND)
                    .body("Delete failed: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                    .body("Error deleting Question: " + e.getMessage());
        }
    }
}
