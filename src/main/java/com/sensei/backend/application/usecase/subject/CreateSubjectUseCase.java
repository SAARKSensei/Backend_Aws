package com.sensei.backend.application.usecase.subject;

import com.sensei.backend.application.dto.subject.SubjectRequestDTO;
import com.sensei.backend.application.dto.subject.SubjectResponseDTO;
import com.sensei.backend.domain.model.SubjectModel;
import com.sensei.backend.domain.port.repository.Subject.SubjectRepositoryPort;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class CreateSubjectUseCase {

    private final SubjectRepositoryPort repository;

    public SubjectResponseDTO execute(SubjectRequestDTO dto) {
        SubjectModel subject = new SubjectModel(
                null,
                dto.getName(),
                dto.getDescription(),
                dto.getIconUrl(),
                true,
                Instant.now()
        );

        return SubjectResponseDTO.fromDomain(
                repository.save(subject)
        );
    }
}
