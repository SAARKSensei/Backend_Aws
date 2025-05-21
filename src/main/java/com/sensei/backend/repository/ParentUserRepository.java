package com.sensei.backend.repository;

import com.sensei.backend.entity.ParentUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Optional;

public interface ParentUserRepository extends JpaRepository<ParentUser, String> {
    Optional<ParentUser> findByUserName(String userName);
    Optional<ParentUser> findByPhone(String phone);  // Added by Vaishnav Kale
    @Query("SELECT p FROM ParentUser p LEFT JOIN FETCH p.childUsers WHERE p.phone = :phoneNumber")   // Added by Vaishnav Kale
    Optional<ParentUser> findByPhoneNumberWithChildUsers(@Param("phoneNumber") String phoneNumber);   // Added by Vaishnav Kale
    Optional<ParentUser> findByEmail(@Param("email") String email);   // Added by Vaishnav Kale

}
