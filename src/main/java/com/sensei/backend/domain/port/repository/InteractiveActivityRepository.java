// package com.sensei.backend.repository;

// import com.sensei.backend.entity.InteractiveActivity;
// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.stereotype.Repository;

// @Repository
// public interface InteractiveActivityRepository extends JpaRepository<InteractiveActivity, String> {
// }


// package com.sensei.backend.repository;

// import com.sensei.backend.entity.InteractiveActivity;
// import com.sensei.backend.entity.SubModule;
// import org.springframework.data.jpa.repository.JpaRepository;

// import java.util.List;
// import java.util.UUID;



// public interface InteractiveActivityRepository extends JpaRepository<InteractiveActivity, UUID> {
    
   
//     List<InteractiveActivity> findBySubModule(SubModule subModule);
//     // 🔒 Enforcement rule support
//     long countBySubModuleId(UUID subModuleId);

// }

package com.sensei.backend.domain.port.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sensei.backend.adapter.persistence.jpa.entity.InteractiveActivity;
import com.sensei.backend.adapter.persistence.jpa.entity.SubModule;

import java.util.List;
import java.util.UUID;

public interface InteractiveActivityRepository extends JpaRepository<InteractiveActivity, UUID> {

    // UI / API use
    List<InteractiveActivity> findBySubModule_IdAndIsActiveTrueOrderByOrderIndexAsc(UUID subModuleId);

    // Internal rule checks
    List<InteractiveActivity> findBySubModule(SubModule subModule);

    long countBySubModuleId(UUID subModuleId);
}
