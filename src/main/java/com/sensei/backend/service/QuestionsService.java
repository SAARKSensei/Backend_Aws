package com.sensei.backend.service;

import com.sensei.backend.entity.DigitalActivity;
import com.sensei.backend.entity.Question;
import com.sensei.backend.dto.QuestionsDTO;
import com.sensei.backend.repository.QuestionRepository;
import com.sensei.backend.repository.DigitalActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class QuestionsService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private DigitalActivityRepository digitalActivityRepository;

    // Create question linked to DigitalActivity
    public Question createQuestion(Question question, String digitalActivityId) {
        if (question.getQuestionId() == null || question.getQuestionId().isEmpty()) {
            question.setQuestionId(UUID.randomUUID().toString());
        }

        DigitalActivity digitalActivity = digitalActivityRepository.findById(digitalActivityId)
                .orElseThrow(() -> new RuntimeException("Digital Activity not found with ID: " + digitalActivityId));

        question.setDigitalActivity(digitalActivity);

        // Always keep hints as NULL
        question.setOption1Hint(null);
        question.setOption2Hint(null);
        question.setOption3Hint(null);

        autoSetOptionStatuses(question);

        return questionRepository.save(question);
    }

    // Create from DTO
    public QuestionsDTO createQuestionFromDTO(QuestionsDTO dto, String digitalActivityId) {
        Question q = new Question();
        q.setQuestionId(dto.getQuestionId() != null ? dto.getQuestionId() : UUID.randomUUID().toString());
        q.setQuestion(dto.getQuestionName());
        q.setSenseiQuestion(dto.getSenseiQuestion());
        q.setSenseiAnswer(dto.getSenseiAnswer());
        q.setCorrectAnswerDescription(dto.getCorrectAnswerDescription());
        q.setIncorrectAnswerDescription(dto.getIncorrectAnswerDescription());
        q.setQuestionImage(dto.getQuestionImage());
        q.setQuestionNumber(dto.getQuestionNumber());

        q.setOption1(dto.getOption1());
        q.setOption1CounsellorNote(dto.getOption1CounsellorNote());
        q.setOption2(dto.getOption2());
        q.setOption2CounsellorNote(dto.getOption2CounsellorNote());
        q.setOption3(dto.getOption3());
        q.setOption3CounsellorNote(dto.getOption3CounsellorNote());

        // Hints always NULL
        q.setOption1Hint(null);
        q.setOption2Hint(null);
        q.setOption3Hint(null);

        DigitalActivity activity = digitalActivityRepository.findById(digitalActivityId)
                .orElseThrow(() -> new RuntimeException("Digital Activity not found"));
        q.setDigitalActivity(activity);

        autoSetOptionStatuses(q);

        Question saved = questionRepository.save(q);
        return convertToDTO(saved);
    }

    // Get all questions as DTOs
    public List<QuestionsDTO> getAllQuestionsDTO() {
        return questionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<Question> findById(String id) {
        return questionRepository.findById(id);
    }

    public Question updateQuestion(String id, Question updated) {
        Question existing = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        existing.setQuestion(updated.getQuestion());
        existing.setSenseiQuestion(updated.getSenseiQuestion());
        existing.setSenseiAnswer(updated.getSenseiAnswer());
        existing.setCorrectAnswerDescription(updated.getCorrectAnswerDescription());
        existing.setIncorrectAnswerDescription(updated.getIncorrectAnswerDescription());
        existing.setQuestionImage(updated.getQuestionImage());
        existing.setOption1(updated.getOption1());
        existing.setOption1CounsellorNote(updated.getOption1CounsellorNote());
        existing.setOption2(updated.getOption2());
        existing.setOption2CounsellorNote(updated.getOption2CounsellorNote());
        existing.setOption3(updated.getOption3());
        existing.setOption3CounsellorNote(updated.getOption3CounsellorNote());
        existing.setQuestionNumber(updated.getQuestionNumber());

        // Always NULL for hints
        existing.setOption1Hint(null);
        existing.setOption2Hint(null);
        existing.setOption3Hint(null);

        autoSetOptionStatuses(existing);
        return questionRepository.save(existing);
    }

    public void deleteQuestion(String id) {
        questionRepository.deleteById(id);
    }

    // Convert entity → DTO
    public QuestionsDTO convertToDTO(Question q) {
        QuestionsDTO dto = new QuestionsDTO();
        dto.setQuestionId(q.getQuestionId());
        dto.setQuestionNumber(q.getQuestionNumber());
        dto.setQuestionName(q.getQuestion() != null ? q.getQuestion() : q.getSenseiQuestion());
        dto.setSenseiQuestion(q.getSenseiQuestion());
        dto.setSenseiAnswer(q.getSenseiAnswer());
        dto.setCorrectAnswerDescription(q.getCorrectAnswerDescription());
        dto.setIncorrectAnswerDescription(q.getIncorrectAnswerDescription());
        dto.setQuestionImage(q.getQuestionImage());

        dto.setOption1(q.getOption1());
        dto.setOption1Status(q.getOption1Status());
        dto.setOption1CounsellorNote(q.getOption1CounsellorNote());

        dto.setOption2(q.getOption2());
        dto.setOption2Status(q.getOption2Status());
        dto.setOption2CounsellorNote(q.getOption2CounsellorNote());

        dto.setOption3(q.getOption3());
        dto.setOption3Status(q.getOption3Status());
        dto.setOption3CounsellorNote(q.getOption3CounsellorNote());

        // Hints not included in DTO

        dto.setDigitalActivityRef(
                q.getDigitalActivity() != null ? q.getDigitalActivity().getDigitalActivityId() : null
        );

        dto.setDigitalActivityIdRef(null);
        dto.setNextQuestionIdRef(null);

        return dto;
    }

    // Auto mark correct/wrong
    public void autoSetOptionStatuses(Question q) {
        String correct = normalize(q.getSenseiAnswer());

        if (q.getOption1() != null)
            q.setOption1Status(matchAnswer(correct, q.getOption1()) ? "correct" : "wrong");

        if (q.getOption2() != null)
            q.setOption2Status(matchAnswer(correct, q.getOption2()) ? "correct" : "wrong");

        if (q.getOption3() != null)
            q.setOption3Status(matchAnswer(correct, q.getOption3()) ? "correct" : "wrong");
    }

    private boolean matchAnswer(String correct, String option) {
        if (option == null) return false;
        return normalize(correct).equalsIgnoreCase(normalize(option));
    }

    private String normalize(String s) {
        return s == null ? "" : s.trim().replaceAll("\\s+", " ");
    }
    
    public QuestionsDTO convertToDTOWithoutHints(Question q) {
        QuestionsDTO dto = new QuestionsDTO();

        dto.setQuestionId(q.getQuestionId());
        dto.setQuestionNumber(q.getQuestionNumber());
        dto.setSenseiQuestion(q.getSenseiQuestion());

        dto.setOption1(q.getOption1());
        dto.setOption1Status(q.getOption1Status());
        dto.setOption1CounsellorNote(q.getOption1CounsellorNote());
        dto.setOption1Hint(null);   // ❌ Always null

        dto.setOption2(q.getOption2());
        dto.setOption2Status(q.getOption2Status());
        dto.setOption2CounsellorNote(q.getOption2CounsellorNote());
        dto.setOption2Hint(null);   // ❌ Always null

        dto.setOption3(q.getOption3());
        dto.setOption3Status(q.getOption3Status());
        dto.setOption3CounsellorNote(q.getOption3CounsellorNote());
        dto.setOption3Hint(null);   // ❌ Always null

        dto.setSenseiAnswer(q.getSenseiAnswer());
        dto.setCorrectAnswerDescription(q.getCorrectAnswerDescription());
        dto.setIncorrectAnswerDescription(q.getIncorrectAnswerDescription());
        dto.setQuestionImage(q.getQuestionImage());

        
        dto.setDigitalActivityIdRef(q.getDigitalActivityIdRef());

        return dto;
    }

}
