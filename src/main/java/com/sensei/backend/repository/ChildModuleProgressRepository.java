package com.sensei.backend.repository;

import com.sensei.backend.entity.ChildModuleProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Repository
public interface ChildModuleProgressRepository extends JpaRepository<ChildModuleProgress, UUID> {
    Optional<ChildModuleProgress> findByChildIdAndModuleId(UUID childId, UUID moduleId);
    long countByChildIdAndModuleIdInAndIsCompletedTrue(UUID childId, List<UUID> moduleIds);
}
