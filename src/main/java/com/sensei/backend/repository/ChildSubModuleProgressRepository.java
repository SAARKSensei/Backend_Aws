package com.sensei.backend.repository;

import com.sensei.backend.entity.ChildSubModuleProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Repository
public interface ChildSubModuleProgressRepository extends JpaRepository<ChildSubModuleProgress, UUID> {
    Optional<ChildSubModuleProgress> findByChildIdAndSubModuleId(UUID childId, UUID subModuleId);
    long countByChildIdAndSubModuleIdInAndIsCompletedTrue(UUID childId, List<UUID> subModuleIds);
}
