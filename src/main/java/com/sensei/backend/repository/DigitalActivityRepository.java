package com.sensei.backend.repository;

import com.sensei.backend.entity.DigitalActivity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DigitalActivityRepository extends JpaRepository<DigitalActivity, UUID> {

    // For APIs
    List<DigitalActivity> findBySubModule_IdAndIsActiveTrueOrderByOrderIndexAsc(UUID subModuleId);

    // For enforcement rules
    long countBySubModule_Id(UUID subModuleId);
}
