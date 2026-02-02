package com.sensei.backend.adapter.persistence.jpa.repository.Subject;

import com.sensei.backend.adapter.persistence.jpa.entity.SubjectEntity;
import com.sensei.backend.domain.port.repository.Subject.SubjectRepositoryPort;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class SubjectJpaRepositoryAdapter implements SubjectRepositoryPort {

    private final SubjectJpaRepository subjectJpaRepository;

    @Override
    public com.sensei.backend.domain.model.SubjectModel save(
            com.sensei.backend.domain.model.SubjectModel domain) {
        SubjectEntity entity = toEntity(domain);
        SubjectEntity saved = subjectJpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<com.sensei.backend.domain.model.SubjectModel> findById(UUID id) {
        return subjectJpaRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public List<com.sensei.backend.domain.model.SubjectModel> findActive() {
        return subjectJpaRepository.findByIsActiveTrue()
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    // ===== Mapping =====

    private SubjectEntity toEntity(com.sensei.backend.domain.model.SubjectModel domain) {
        return SubjectEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .description(domain.getDescription())
                .iconUrl(domain.getIconUrl())
                .isActive(domain.isActive())
                .createdAt(
                        domain.getCreatedAt() == null
                                ? null
                                : domain.getCreatedAt()
                                        .atZone(ZoneOffset.UTC)
                                        .toLocalDateTime())
                .build();
    }

    private com.sensei.backend.domain.model.SubjectModel toDomain(SubjectEntity entity) {
        return new com.sensei.backend.domain.model.SubjectModel(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getIconUrl(),
                Boolean.TRUE.equals(entity.getIsActive()),
                entity.getCreatedAt() == null
                        ? null
                        : entity.getCreatedAt()
                                .atZone(ZoneOffset.UTC)
                                .toInstant());
    }

}
