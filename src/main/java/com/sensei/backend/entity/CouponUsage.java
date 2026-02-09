package com.sensei.backend.entity;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "coupon_usage",
       uniqueConstraints = @UniqueConstraint(columnNames = {"coupon_id", "parent_id"}))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponUsage {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "coupon_id", nullable = false)
    private UUID couponId;

    @Column(name = "parent_id", nullable = false)
    private UUID parentId;

    @Column(name = "pricing_plan_id", nullable = false)
    private UUID pricingPlanId;

    @Column(name = "master_transaction_id")
    private UUID masterTransactionId;

    @Column(name = "discount_amount", nullable = false)
    private Integer discountAmount;

    @Column(name = "used_at")
    private LocalDateTime usedAt;
}
