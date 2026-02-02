package com.sensei.backend.domain.service;

import java.util.List;
import java.util.UUID;

import com.sensei.backend.application.dto.questionOption.QuestionOptionRequestDTO;
import com.sensei.backend.application.dto.questionOption.QuestionOptionResponseDTO;

public interface QuestionOptionService {

    QuestionOptionResponseDTO create(QuestionOptionRequestDTO dto);

    QuestionOptionResponseDTO update(UUID id, QuestionOptionRequestDTO dto);

    void delete(UUID id);

    List<QuestionOptionResponseDTO> getByQuestion(UUID questionId);
}
