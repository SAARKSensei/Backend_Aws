package com.sensei.backend.domain.port.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sensei.backend.adapter.persistence.jpa.entity.DigitalActivity;

import java.util.List;
import java.util.UUID;

public interface DigitalActivityRepository extends JpaRepository<DigitalActivity, UUID> {

    // For APIs
    List<DigitalActivity> findBySubModule_IdAndIsActiveTrueOrderByOrderIndexAsc(UUID subModuleId);

    // For enforcement rules
    long countBySubModule_Id(UUID subModuleId);
}
