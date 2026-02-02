package com.sensei.backend.application.usecase.subject;

import com.sensei.backend.application.dto.subject.SubjectResponseDTO;
import com.sensei.backend.domain.port.repository.Subject.SubjectRepositoryPort;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetAllSubjectsUseCase {

    private final SubjectRepositoryPort repository;

    public List<SubjectResponseDTO> execute() {
        return repository.findActive()
                .stream()
                .map(SubjectResponseDTO::fromDomain)
                .collect(Collectors.toList());
    }
}

// TODO: GET by ID