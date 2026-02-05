package com.sensei.backend.repository;

import com.sensei.backend.entity.ChildSubModuleCompletion;
import com.sensei.backend.entity.SubModule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChildSubModuleCompletionRepository
        extends JpaRepository<ChildSubModuleCompletion, UUID> {

    boolean existsByChildIdAndSubModule(UUID childId, SubModule subModule);
}
// This supports:

// unlock logic

// certification

// progress %