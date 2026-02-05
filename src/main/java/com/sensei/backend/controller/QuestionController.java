// package com.sensei.backend.controller;

// import com.sensei.backend.entity.Questions;
// import com.sensei.backend.dto.QuestionsDTO;
// import com.sensei.backend.service.QuestionsService;
// import org.apache.http.HttpStatus;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;
// import javax.validation.Valid;
// import java.util.List;
// import java.util.Optional;

// @RestController
// @RequestMapping("/api/questions")
// public class QuestionsController {

//     @Autowired
//     private QuestionsService questionsService;

//     // ✅ Create Question directly
//     @PostMapping
//     public ResponseEntity<Questions> createQuestion(@Valid @RequestBody Questions questions) {
//         Questions saved = questionsService.createQuestion(questions, questions.getDigitalActivityIdRef());
//         return ResponseEntity.status(201).body(saved);
//     }

//     // ✅ Create Question using DTO
//     @PostMapping("/dto")
//     public ResponseEntity<QuestionsDTO> createQuestionFromDTO(
//             @Valid @RequestBody QuestionsDTO dto,
//             @RequestParam String digitalActivityId) {

//         QuestionsDTO savedDto = questionsService.createQuestionFromDTO(dto, digitalActivityId);
//         return ResponseEntity.status(201).body(savedDto);
//     }

//     // ✅ Get all questions
//     @GetMapping
//     public ResponseEntity<List<QuestionsDTO>> getAllQuestions() {
//         List<QuestionsDTO> dtos = questionsService.getAllQuestionsDTO();
//         return ResponseEntity.ok().body(dtos);
//     }

//     // ✅ Get question by ID
//     @GetMapping("/{id}")
//     public ResponseEntity<?> getQuestionById(@PathVariable String id) {
//         try {
//             Optional<Questions> opt = questionsService.findById(id);
//             if (opt.isPresent()) {
//                 return ResponseEntity.ok(opt.get());
//             } else {
//                 return ResponseEntity.status(HttpStatus.SC_NOT_FOUND)
//                         .body("Question not found with id: " + id);
//             }
//         } catch (Exception e) {
//             return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
//                     .body("Error fetching Question: " + e.getMessage());
//         }
//     }

//     // ✅ Update Question
//     @PutMapping("/{id}")
//     public ResponseEntity<?> updateQuestion(@PathVariable String id, @Valid @RequestBody Questions updated) {
//         try {
//             Questions saved = questionsService.updateQuestion(id, updated);
//             return ResponseEntity.ok(saved);
//         } catch (RuntimeException e) {
//             return ResponseEntity.status(HttpStatus.SC_NOT_FOUND)
//                     .body("Update failed: " + e.getMessage());
//         } catch (Exception e) {
//             return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
//                     .body("Error updating Question: " + e.getMessage());
//         }
//     }

//     // ✅ Delete Question
//     @DeleteMapping("/{id}")
//     public ResponseEntity<?> deleteQuestion(@PathVariable String id) {
//         try {
//             questionsService.deleteQuestion(id);
//             return ResponseEntity.noContent().build();
//         } catch (RuntimeException e) {
//             return ResponseEntity.status(HttpStatus.SC_NOT_FOUND)
//                     .body("Delete failed: " + e.getMessage());
//         } catch (Exception e) {
//             return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
//                     .body("Error deleting Question: " + e.getMessage());
//         }
//     }
// }

package com.sensei.backend.controller;

import com.sensei.backend.dto.question.QuestionRequestDTO;
import com.sensei.backend.dto.question.QuestionResponseDTO;
import com.sensei.backend.service.AccessControlService;
import com.sensei.backend.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService service;
    private final AccessControlService accessControlService;

    @PostMapping
    public QuestionResponseDTO create(@RequestBody QuestionRequestDTO dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public QuestionResponseDTO update(
            @PathVariable UUID id,
            @RequestBody QuestionRequestDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }

    @GetMapping("/{id}")
    public QuestionResponseDTO getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @GetMapping("/digital-activity/{digitalActivityId}")
    public List<QuestionResponseDTO> getByDigitalActivity(
            @PathVariable UUID digitalActivityId,
            @RequestParam UUID childId) {

        UUID subjectId = service.getSubjectIdByDigitalActivity(digitalActivityId);
        accessControlService.validateSubjectAccess(childId, subjectId);

        return service.getByDigitalActivity(digitalActivityId);
    }
}

