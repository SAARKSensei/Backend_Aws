package com.sensei.backend.domain.port.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sensei.backend.adapter.persistence.jpa.entity.InteractiveProcess;

import java.util.List;
import java.util.UUID;

public interface InteractiveProcessRepository extends JpaRepository<InteractiveProcess, UUID> {

    List<InteractiveProcess> findByInteractiveActivityIdOrderByStepOrderAsc(UUID activityId);

    List<InteractiveProcess> findByChildId(UUID childId);

    long countByInteractiveActivityId(UUID activityId);

    List<InteractiveProcess> findBySubModuleIdOrderByStepOrderAsc(UUID subModuleId);
}



