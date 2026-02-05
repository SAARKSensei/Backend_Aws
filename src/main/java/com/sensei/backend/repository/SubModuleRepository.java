// package com.sensei.backend.repository;

// import com.sensei.backend.entity.SubModule;
// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.stereotype.Repository;

// @Repository
// public interface SubModuleRepository extends JpaRepository<SubModule, String> {
// }
package com.sensei.backend.repository;

import com.sensei.backend.entity.SubModule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SubModuleRepository extends JpaRepository<SubModule, UUID> {

    List<SubModule> findByModule_IdAndIsActiveTrueOrderByOrderIndexAsc(UUID moduleId);

    List<SubModule> findByIsActiveTrueOrderByCreatedAtDesc();
}
