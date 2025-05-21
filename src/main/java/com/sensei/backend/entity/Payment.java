package com.sensei.backend.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name="system-uuid",strategy = "uuid")
    @Column(columnDefinition = "TEXT")
    private String id;
    // @Column(name = "razorpay_order_id")    // Added by Vaishnav Kale
    private String razorpayOrderId;
    // @Column(name = "razorpay_payment_id")   // Added by Vaishnav Kale
    private String razorpayPaymentId;
    // @Column(name = "razorpay_signature")    // Added by Vaishnav Kale
    private String razorpaySignature;

    private double amount;

    // @Column(name = "payment_date")      // Added by Vaishnav Kale
    private LocalDateTime paymentDate;
}
