package com.sensei.backend.application.usecase.subject;

import com.sensei.backend.application.dto.subject.SubjectRequestDTO;
import com.sensei.backend.application.dto.subject.SubjectResponseDTO;
import com.sensei.backend.domain.model.SubjectModel;
import com.sensei.backend.domain.port.repository.Subject.SubjectRepositoryPort;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateSubjectUseCase {

    private final SubjectRepositoryPort repository;

    public SubjectResponseDTO execute(UUID id, SubjectRequestDTO dto) {
        SubjectModel subject = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        subject.update(
                dto.getName(),
                dto.getDescription(),
                dto.getIconUrl()
        );

        return SubjectResponseDTO.fromDomain(
                repository.save(subject)
        );
    }
}
