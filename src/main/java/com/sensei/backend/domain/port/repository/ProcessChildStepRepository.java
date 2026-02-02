package com.sensei.backend.domain.port.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sensei.backend.adapter.persistence.jpa.entity.ProcessChildStep;

import java.util.List;
import java.util.UUID;

public interface ProcessChildStepRepository extends JpaRepository<ProcessChildStep, UUID> {

    List<ProcessChildStep> findByProcessIdOrderByStepOrderAsc(UUID processId);

    List<ProcessChildStep> findByProcess_ChildIdOrderByStartedAtAsc(UUID childId);
}
