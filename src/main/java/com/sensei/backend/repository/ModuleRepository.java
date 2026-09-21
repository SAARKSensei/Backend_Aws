// package com.sensei.backend.repository;

// import com.sensei.backend.entity.Module;
// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.stereotype.Repository;

// @Repository
// public interface ModuleRepository extends JpaRepository<Module, String> {
// }
package com.sensei.backend.repository;

import com.sensei.backend.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;
import java.util.UUID;

public interface ModuleRepository extends JpaRepository<Module, UUID> {

    @EntityGraph(attributePaths = {"subject"})
    List<Module> findBySubjectIdAndIsActiveTrueOrderByOrderIndexAsc(UUID subjectId);
    
    @EntityGraph(attributePaths = {"subject"})
    List<Module> findByIsActiveTrueOrderByCreatedAtDesc();
}


