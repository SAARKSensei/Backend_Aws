package com.sensei.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "digital_activity_result_calculator")
public class DigitalActivityResultCalculator {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @Column(nullable = false)
    private String childId;   // changed from studentId → childId

    @Column(columnDefinition = "TEXT")
    private String digitalActivityId;

    private double totalTimeTaken;  
    private Double result;          
    private int numberOfAttempts;   

    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
