package com.sensei.backend.adapter.persistence.jpa.repository.Subject;

import com.sensei.backend.adapter.persistence.jpa.entity.SubjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SubjectJpaRepository
        extends JpaRepository<SubjectEntity, UUID> {

    List<SubjectEntity> findByIsActiveTrue();
}
