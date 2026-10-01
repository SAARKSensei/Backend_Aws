package com.sensei.backend.repository;

import com.sensei.backend.entity.ChildSubjectProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChildSubjectProgressRepository extends JpaRepository<ChildSubjectProgress, UUID> {
    Optional<ChildSubjectProgress> findByChildIdAndSubjectId(UUID childId, UUID subjectId);
}
