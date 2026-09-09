package com.sensei.backend.repository;

import com.sensei.backend.entity.ChildSubModuleCompletion;
import com.sensei.backend.entity.SubModule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import java.util.Optional;
public interface ChildSubModuleCompletionRepository
        extends JpaRepository<ChildSubModuleCompletion, UUID> {

    boolean existsByChildIdAndSubModule(UUID childId, SubModule subModule);

// This supports:

// unlock logic

// certification

// progress %

long countByChildId(UUID childId);

Optional<ChildSubModuleCompletion> findByChildIdAndSubModule(
        UUID childId,
        SubModule subModule
);

java.util.List<ChildSubModuleCompletion> findByChildId(UUID childId);

}