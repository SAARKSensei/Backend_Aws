package com.sensei.backend.application.usecase.subject;

import com.sensei.backend.application.dto.subject.SubjectResponseDTO;
import com.sensei.backend.domain.model.SubjectModel;
import com.sensei.backend.domain.port.repository.Subject.SubjectRepositoryPort;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetSubjectByIdUseCase {

    private final SubjectRepositoryPort repository;

    public SubjectResponseDTO execute(UUID id) {
        SubjectModel subject = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        return SubjectResponseDTO.fromDomain(subject);
    }
}
