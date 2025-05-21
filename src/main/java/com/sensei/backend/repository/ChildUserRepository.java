package com.sensei.backend.repository;

import com.sensei.backend.entity.ChildUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChildUserRepository extends JpaRepository<ChildUser, String> {
    List<ChildUser> findByPhoneNumber(long phoneNumber);
}
