// package com.sensei.backend.repository;

// import com.sensei.backend.entity.Processes;
// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.stereotype.Repository;

// @Repository
// public interface ProcessesRepository extends JpaRepository<Processes, String> {
// }
package com.sensei.backend.domain.port.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sensei.backend.adapter.persistence.jpa.entity.InteractiveActivity;
import com.sensei.backend.adapter.persistence.jpa.entity.Process;

import java.util.List;
import java.util.UUID;

public interface ProcessRepository extends JpaRepository<Process, UUID> {

    List<Process> findByInteractiveActivityOrderByStepOrder(InteractiveActivity interactiveActivity);
}
