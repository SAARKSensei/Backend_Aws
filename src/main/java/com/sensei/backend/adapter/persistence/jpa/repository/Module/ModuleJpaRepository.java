package com.sensei.backend.adapter.persistence.jpa.repository.Module;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sensei.backend.adapter.persistence.jpa.entity.ModuleEntity;

import java.util.List;
import java.util.UUID;

public interface ModuleJpaRepository extends JpaRepository<ModuleEntity, UUID> {

    List<ModuleEntity> findBySubjectIdAndIsActiveTrueOrderByOrderIndexAsc(UUID subjectId);

    List<ModuleEntity> findByIsActiveTrueOrderByCreatedAtDesc();
}
