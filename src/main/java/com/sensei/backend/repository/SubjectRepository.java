// package com.sensei.backend.repository;

// import com.sensei.backend.entity.Subject;
// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.stereotype.Repository;

// @Repository
// public interface SubjectRepository extends JpaRepository<Subject, String> {
// }
package com.sensei.backend.repository;

import com.sensei.backend.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;        // ✅ REQUIRED
import java.util.UUID;

public interface SubjectRepository extends JpaRepository<Subject, UUID> {

    List<Subject> findByIsActiveTrue();
}
