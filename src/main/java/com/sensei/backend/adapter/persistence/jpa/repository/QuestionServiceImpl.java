package com.sensei.backend.adapter.persistence.jpa.repository;

import com.sensei.backend.adapter.persistence.jpa.entity.DigitalActivity;
import com.sensei.backend.adapter.persistence.jpa.entity.Question;
import com.sensei.backend.adapter.persistence.jpa.entity.QuestionOption;
import com.sensei.backend.application.dto.question.QuestionRequestDTO;
import com.sensei.backend.application.dto.question.QuestionResponseDTO;
import com.sensei.backend.application.dto.questionOption.QuestionOptionResponseDTO;
import com.sensei.backend.domain.port.repository.DigitalActivityRepository;
import com.sensei.backend.domain.port.repository.QuestionOptionRepository;
import com.sensei.backend.domain.port.repository.QuestionRepository;
import com.sensei.backend.domain.service.QuestionService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionOptionRepository optionRepository;
    private final DigitalActivityRepository digitalActivityRepository;

    private QuestionResponseDTO map(Question q) {
        QuestionResponseDTO dto = new QuestionResponseDTO();
        dto.setId(q.getId());
        dto.setDigitalActivityId(q.getDigitalActivity().getId());
        dto.setQuestionText(q.getQuestionText());
        dto.setHint(q.getHint());
        dto.setCounsellorNote(q.getCounsellorNote());
        dto.setExplanation(q.getExplanation());
        dto.setOrderIndex(q.getOrderIndex());

        List<QuestionOptionResponseDTO> options =
                optionRepository.findByQuestionIdOrderByOrderIndexAsc(q.getId())
                        .stream()
                        .map(o -> {
                            QuestionOptionResponseDTO odto = new QuestionOptionResponseDTO();
                            odto.setId(o.getId());
                            odto.setOptionText(o.getOptionText());
                            odto.setCorrect(o.getIsCorrect());
                            odto.setHint(o.getHint());
                            odto.setCounsellorNote(o.getCounsellorNote());
                            odto.setOrderIndex(o.getOrderIndex());
                            return odto;
                        })
                        .collect(Collectors.toList());

        dto.setOptions(options);
        return dto;
    }

    @Override
    public QuestionResponseDTO create(QuestionRequestDTO dto) {
        DigitalActivity activity = digitalActivityRepository.findById(dto.getDigitalActivityId())
                .orElseThrow(() -> new RuntimeException("Digital Activity not found"));

        Question q = Question.builder()
                .digitalActivity(activity)
                .questionText(dto.getQuestionText())
                .hint(dto.getHint())
                .counsellorNote(dto.getCounsellorNote())
                .explanation(dto.getExplanation())
                .orderIndex(dto.getOrderIndex())
                .build();

        return map(questionRepository.save(q));
    }

    @Override
    public QuestionResponseDTO update(UUID id, QuestionRequestDTO dto) {
        Question q = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        q.setQuestionText(dto.getQuestionText());
        q.setHint(dto.getHint());
        q.setCounsellorNote(dto.getCounsellorNote());
        q.setExplanation(dto.getExplanation());
        q.setOrderIndex(dto.getOrderIndex());

        return map(questionRepository.save(q));
    }

    @Override
    public void delete(UUID id) {
        questionRepository.deleteById(id);
    }

    @Override
    public QuestionResponseDTO getById(UUID id) {
        return map(questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found")));
    }

    @Override
    public List<QuestionResponseDTO> getByDigitalActivity(UUID digitalActivityId) {
        return questionRepository.findByDigitalActivityIdOrderByOrderIndexAsc(digitalActivityId)
                .stream().map(this::map).collect(Collectors.toList());
    }
}
