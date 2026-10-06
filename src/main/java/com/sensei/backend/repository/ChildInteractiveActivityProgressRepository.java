package com.sensei.backend.repository;

import com.sensei.backend.entity.ChildInteractiveActivityProgress;
import com.sensei.backend.entity.InteractiveActivity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

public interface ChildInteractiveActivityProgressRepository
        extends JpaRepository<ChildInteractiveActivityProgress, UUID> {

    Optional<ChildInteractiveActivityProgress>
    findByChildIdAndInteractiveActivity(UUID childId, InteractiveActivity activity);
    long countByChildIdAndStatus(UUID childId, String status);

    long countByChildIdAndInteractiveActivity_SubModule_IdAndStatus(
    UUID childId,
    UUID subModuleId,
    String status
);        

    long countByChildIdAndInteractiveActivity_SubModule_IdAndInteractiveActivity_IsActiveTrueAndStatus(
            UUID childId,
            UUID subModuleId,
            String status
    );

List<ChildInteractiveActivityProgress> findByChildId(UUID childId);
}
