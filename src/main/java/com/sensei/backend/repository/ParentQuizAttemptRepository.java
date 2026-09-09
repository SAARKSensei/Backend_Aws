package com.sensei.backend.repository;

import com.sensei.backend.entity.ParentQuizAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ParentQuizAttemptRepository extends JpaRepository<ParentQuizAttempt, UUID> {
    Optional<ParentQuizAttempt> findByParentUser_ParentIdAndChildUser_ChildId(UUID parentId, UUID childId);
    boolean existsByParentUser_ParentIdAndChildUser_ChildId(UUID parentId, UUID childId);
}
