package com.sensei.backend.service;

import com.sensei.backend.dto.QuestionsDTO;
import com.sensei.backend.entity.Questions;
import com.sensei.backend.repository.QuestionsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuestionsService {

    @Autowired
    private QuestionsRepository questionsRepository;

    @Autowired
    private ModelMapper modelMapper;

    public QuestionsDTO createQuestions(QuestionsDTO questionsDTO) {
        Questions questions = modelMapper.map(questionsDTO, Questions.class);
        Questions savedQuestions = questionsRepository.save(questions);
        return modelMapper.map(savedQuestions, QuestionsDTO.class);
    }

    public List<QuestionsDTO> getAllQuestions() {
        return questionsRepository.findAll().stream()
                .map(questions -> modelMapper.map(questions, QuestionsDTO.class))
                .collect(Collectors.toList());
    }

    public Optional<QuestionsDTO> getQuestionsById(String id) {
        return questionsRepository.findById(id)
                .map(questions -> modelMapper.map(questions, QuestionsDTO.class));
    }

    public QuestionsDTO updateQuestions(String id, QuestionsDTO questionsDTO) {
        Questions questions = modelMapper.map(questionsDTO, Questions.class);
        questions.setQuestionId(id);
        Questions updatedQuestions = questionsRepository.save(questions);
        return modelMapper.map(updatedQuestions, QuestionsDTO.class);
    }

    public void deleteQuestions(String id) {
        questionsRepository.deleteById(id);
    }
}
