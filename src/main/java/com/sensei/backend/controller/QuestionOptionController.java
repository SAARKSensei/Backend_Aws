package com.sensei.backend.controller;

import com.sensei.backend.dto.questionOption.QuestionOptionRequestDTO;
import com.sensei.backend.dto.questionOption.QuestionOptionResponseDTO;
import com.sensei.backend.service.QuestionOptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/question-options")
@RequiredArgsConstructor
public class QuestionOptionController {

    private final QuestionOptionService service;

    @PostMapping
    public QuestionOptionResponseDTO create(@RequestBody QuestionOptionRequestDTO dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public QuestionOptionResponseDTO update(@PathVariable UUID id, @RequestBody QuestionOptionRequestDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }

    @GetMapping("/question/{questionId}")
    public List<QuestionOptionResponseDTO> getByQuestion(@PathVariable UUID questionId) {
        return service.getByQuestion(questionId);
    }
}