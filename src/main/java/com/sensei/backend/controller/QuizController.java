package com.sensei.backend.controller;

import com.sensei.backend.entity.Question;
import com.sensei.backend.entity.LifeSkill;
import com.sensei.backend.dto.QuizResultDTO;
import com.sensei.backend.dto.QuizSubmitRequest;
import com.sensei.backend.service.QuizResultService;
import com.sensei.backend.repository.QuestionRepository;
import com.sensei.backend.repository.LifeSkillRepository;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.*;

@RestController
@RequestMapping("/api")
public class QuizController {

    private final QuestionRepository questionRepository;
    private final QuizResultService quizResultService;
    private final LifeSkillRepository lifeSkillRepository;

    public QuizController(QuestionRepository questionRepository,
                          QuizResultService quizResultService,
                          LifeSkillRepository lifeSkillRepository) {
        this.questionRepository = questionRepository;
        this.quizResultService = quizResultService;
        this.lifeSkillRepository = lifeSkillRepository;
    }

    // ✅ Create new Question (with auto LifeSkill linking)
    @PostMapping("/question")
    public ResponseEntity<?> createQuestion(@RequestBody Question question) {
        try {
            // ✅ Handle Question-level LifeSkill
            if (question.getLifeSkill() != null && question.getLifeSkill().getLifeskillName() != null) {
                String skillName = question.getLifeSkill().getLifeskillName().trim();
                LifeSkill lifeSkill = lifeSkillRepository
                        .findByLifeskillNameIgnoreCase(skillName)
                        .orElseGet(() -> {
                            LifeSkill newSkill = new LifeSkill();
                            newSkill.setLifeskillName(skillName);
                            return lifeSkillRepository.save(newSkill);
                        });
                question.setLifeSkill(lifeSkill);
            }

            // ✅ Handle Answer-level LifeSkill
            if (question.getAnswers() != null) {
                question.getAnswers().forEach(answer -> {
                    if (answer.getLifeSkill() != null && answer.getLifeSkill().getLifeskillName() != null) {
                        String skillName = answer.getLifeSkill().getLifeskillName().trim();
                        LifeSkill lifeSkill = lifeSkillRepository
                                .findByLifeskillNameIgnoreCase(skillName)
                                .orElseGet(() -> {
                                    LifeSkill newSkill = new LifeSkill();
                                    newSkill.setLifeskillName(skillName);
                                    return lifeSkillRepository.save(newSkill);
                                });
                        answer.setLifeSkill(lifeSkill);
                    }
                    answer.setQuestion(question);
                });
            }

            Question savedQuestion = questionRepository.save(question);
            return ResponseEntity.ok(savedQuestion);

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ Get all questions
    @GetMapping("/question")
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    @PostMapping("/submit")
    public ResponseEntity<Map<String, Object>> submitQuiz(@RequestBody QuizSubmitRequest request) {
        Map<String, Object> result = quizResultService.saveQuizResultAndGetSummary(request);
        return ResponseEntity.ok(result);
    }


    // ✅ Get LifeSkill scores (raw counts)
    @GetMapping("/quiz-results/child/{childId}")
    public ResponseEntity<Map<String, Integer>> getScores(@PathVariable String childId) {
        return ResponseEntity.ok(quizResultService.getLifeSkillScores(childId));
    }

    // ✅ Get all LifeSkills
    @GetMapping("/lifeskills")
    public ResponseEntity<List<LifeSkill>> getAllLifeSkills() {
        return ResponseEntity.ok(lifeSkillRepository.findAll());
    }

}
