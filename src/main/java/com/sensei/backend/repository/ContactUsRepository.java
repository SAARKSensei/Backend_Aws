package com.sensei.backend.repository;

import com.sensei.backend.entity.ContactUs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactUsRepository extends JpaRepository<ContactUs, Long>{
    public ContactUs findByUserName(String userName);
    public ContactUs findByUserNameIgnoreCase(String userName);
}
