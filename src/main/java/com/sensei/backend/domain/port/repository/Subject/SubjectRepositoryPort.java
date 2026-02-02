package com.sensei.backend.domain.port.repository.Subject;

import com.sensei.backend.domain.model.SubjectModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubjectRepositoryPort {

    SubjectModel save(SubjectModel subject);

    Optional<SubjectModel> findById(UUID id);

    List<SubjectModel> findActive();
}
