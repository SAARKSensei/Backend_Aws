package com.sensei.backend.controller;
import com.sensei.backend.service.CouponService;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sensei.backend.dto.coupon.CreateCouponRequestDTO;
import com.sensei.backend.dto.coupon.UpdateCouponRequestDTO;
import com.sensei.backend.dto.coupon.ValidateCouponRequestDTO;
import com.sensei.backend.dto.coupon.ValidateCouponResponseDTO;


@RestController
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    // -------------------------------
    // ADMIN: CREATE COUPON
    // -------------------------------
    @PostMapping("/api/admin/coupons")
    public ResponseEntity<String> createCoupon(
            @RequestBody CreateCouponRequestDTO dto
    ) {
        couponService.createCoupon(dto);
        return ResponseEntity.ok("Coupon created successfully");
    }

    // -------------------------------
    // USER: VALIDATE COUPON
    // -------------------------------
    @PostMapping("/api/coupons/validate")
    public ResponseEntity<ValidateCouponResponseDTO> validateCoupon(
            @RequestBody ValidateCouponRequestDTO dto
    ) {
        return ResponseEntity.ok(couponService.validateCoupon(dto));
    }

    @GetMapping("/api/admin/coupons")
public ResponseEntity<?> listCoupons() {
    return ResponseEntity.ok(couponService.getAllCoupons());
}

@GetMapping("/api/admin/coupons/{id}")
public ResponseEntity<?> getCoupon(@PathVariable UUID id) {
    return ResponseEntity.ok(couponService.getCouponById(id));
}

@PutMapping("/api/admin/coupons")
public ResponseEntity<String> updateCoupon(
        @RequestBody UpdateCouponRequestDTO dto
) {
    couponService.updateCoupon(dto);
    return ResponseEntity.ok("Coupon updated successfully");
}

@PatchMapping("/api/admin/coupons/{id}/disable")
public ResponseEntity<String> disableCoupon(@PathVariable UUID id) {
    couponService.disableCoupon(id);
    return ResponseEntity.ok("Coupon disabled successfully");
}

@DeleteMapping("/api/admin/coupons/{id}")
public ResponseEntity<String> deleteCoupon(@PathVariable UUID id) {
    couponService.deleteCoupon(id);
    return ResponseEntity.ok("Coupon deleted successfully");
}


}
