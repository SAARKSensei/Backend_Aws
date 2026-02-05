package com.sensei.backend.service;

import com.sensei.backend.dto.questionOption.QuestionOptionRequestDTO;
import com.sensei.backend.dto.questionOption.QuestionOptionResponseDTO;

import java.util.List;
import java.util.UUID;

public interface QuestionOptionService {

    QuestionOptionResponseDTO create(QuestionOptionRequestDTO dto);

    QuestionOptionResponseDTO update(UUID id, QuestionOptionRequestDTO dto);

    void delete(UUID id);

    List<QuestionOptionResponseDTO> getByQuestion(UUID questionId);
}
