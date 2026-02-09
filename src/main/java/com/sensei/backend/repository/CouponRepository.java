// package com.sensei.backend.repository;

// import com.sensei.backend.entity.Coupon;
// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.stereotype.Repository;

// import java.util.Optional;

// @Repository
// public interface CouponRepository extends JpaRepository<Coupon, String> {
// 	Optional<Coupon> findByCode(String code);
// }
package com.sensei.backend.repository;

import com.sensei.backend.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CouponRepository extends JpaRepository<Coupon, UUID> {

    Optional<Coupon> findByCodeAndIsActiveTrue(String code);
}
