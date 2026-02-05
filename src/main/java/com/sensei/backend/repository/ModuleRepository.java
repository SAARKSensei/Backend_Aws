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

import java.util.List;
import java.util.UUID;

public interface ModuleRepository extends JpaRepository<Module, UUID> {

    List<Module> findBySubjectIdAndIsActiveTrueOrderByOrderIndexAsc(UUID subjectId);
    List<Module> findByIsActiveTrueOrderByCreatedAtDesc();
}


