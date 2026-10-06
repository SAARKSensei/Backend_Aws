package com.sensei.backend.repository;

import com.sensei.backend.entity.ChildDigitalActivityProgress;
import com.sensei.backend.entity.DigitalActivity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

public interface ChildDigitalActivityProgressRepository 
        extends JpaRepository<ChildDigitalActivityProgress, UUID> {

    Optional<ChildDigitalActivityProgress> 
    findByChildIdAndDigitalActivity(UUID childId, DigitalActivity digitalActivity);

    long countByChildIdAndDigitalActivity_SubModule_IdAndStatus(
            UUID childId,
            UUID subModuleId,
            String status
    );

    long countByChildIdAndDigitalActivity_SubModule_IdAndDigitalActivity_IsActiveTrueAndStatus(
            UUID childId,
            UUID subModuleId,
            String status
    );

    long countByChildIdAndStatus(UUID childId, String status);
    List<ChildDigitalActivityProgress> findByChildId(UUID childId);

}