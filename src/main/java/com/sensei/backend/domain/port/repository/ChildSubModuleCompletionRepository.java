package com.sensei.backend.domain.port.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sensei.backend.adapter.persistence.jpa.entity.ChildSubModuleCompletion;
import com.sensei.backend.adapter.persistence.jpa.entity.SubModule;

import java.util.UUID;

public interface ChildSubModuleCompletionRepository
        extends JpaRepository<ChildSubModuleCompletion, UUID> {

    boolean existsByChildIdAndSubModule(UUID childId, SubModule subModule);
}
// This supports:

// unlock logic

// certification

// progress %