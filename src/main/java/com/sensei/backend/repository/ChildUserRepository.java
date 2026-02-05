// package com.sensei.backend.repository;

// import com.sensei.backend.entity.ChildUser;
// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.stereotype.Repository;

// import java.util.List;

// @Repository
// public interface ChildUserRepository extends JpaRepository<ChildUser, String> {
//     List<ChildUser> findByPhoneNumber(long phoneNumber);
// }

package com.sensei.backend.repository;

import com.sensei.backend.entity.ChildUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChildUserRepository extends JpaRepository<ChildUser, UUID> {

    // 🔹 All children under a parent
    List<ChildUser> findByParentUser_ParentId(UUID parentId);

    // 🔹 Single child safety lookup
    Optional<ChildUser> findByChildId(UUID childId);

    // 🔹 Phone based lookup
    List<ChildUser> findByPhoneNumber(String phoneNumber);

    List<ChildUser> findByParentUserParentId(UUID parentId);

}

