package com.sensei.backend.service.impl;

import com.sensei.backend.dto.questionOption.QuestionOptionRequestDTO;
import com.sensei.backend.dto.questionOption.QuestionOptionResponseDTO;
import com.sensei.backend.entity.Question;
import com.sensei.backend.entity.QuestionOption;
import com.sensei.backend.repository.QuestionOptionRepository;
import com.sensei.backend.repository.QuestionRepository;
import com.sensei.backend.service.QuestionOptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionOptionServiceImpl implements QuestionOptionService {

    private final QuestionOptionRepository repository;
    private final QuestionRepository questionRepository;

    private QuestionOptionResponseDTO map(QuestionOption o) {
        QuestionOptionResponseDTO dto = new QuestionOptionResponseDTO();
        dto.setId(o.getId());
        dto.setOptionText(o.getOptionText());
        dto.setCorrect(o.getIsCorrect());
        dto.setHint(o.getHint());
        dto.setCounsellorNote(o.getCounsellorNote());
        dto.setOrderIndex(o.getOrderIndex());
        dto.setQuestionId(o.getQuestion().getId());
        return dto;
    }

    @Override
    public QuestionOptionResponseDTO create(QuestionOptionRequestDTO dto) {
        Question q = questionRepository.findById(dto.getQuestionId())
                .orElseThrow(() -> new RuntimeException("Question not found"));

        QuestionOption o = QuestionOption.builder()
                .question(q)
                .optionText(dto.getOptionText())
                .isCorrect(dto.getIsCorrect())
                .hint(dto.getHint())
                .counsellorNote(dto.getCounsellorNote())
                .orderIndex(dto.getOrderIndex())
                .build();

        return map(repository.save(o));
    }

    @Override
    public QuestionOptionResponseDTO update(UUID id, QuestionOptionRequestDTO dto) {
        QuestionOption o = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Option not found"));

        o.setOptionText(dto.getOptionText());
        o.setIsCorrect(dto.getIsCorrect());
        o.setHint(dto.getHint());
        o.setCounsellorNote(dto.getCounsellorNote());
        o.setOrderIndex(dto.getOrderIndex());

        return map(repository.save(o));
    }

    // @Override
    // public void delete(UUID id) {
    //     repository.deleteById(id);
    // }

    @Override
    public void delete(UUID id) {
    QuestionOption o = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Option not found"));
            o.setIsActive(false);
            repository.save(o);
}


    // @Override
    // public List<QuestionOptionResponseDTO> getByQuestion(UUID questionId) {
    //     return repository.findByQuestionIdOrderByOrderIndexAsc(questionId)
    //             .stream().map(this::map).collect(Collectors.toList());
    // }

    @Override
public List<QuestionOptionResponseDTO> getByQuestion(UUID questionId) {
    return repository
            .findByQuestion_IdAndIsActiveTrueOrderByOrderIndexAsc(questionId)
            .stream()
            .map(this::map)
            .collect(Collectors.toList());
}


}


