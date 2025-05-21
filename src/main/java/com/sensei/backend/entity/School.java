package com.sensei.backend.entity;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class School {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name="system-uuid",strategy = "uuid")
    private String SchoolId;
    @NotNull
    private String name;
    @NotNull
    private String addressLane1;
    @NotNull
    private String addressLane2;
    @NotNull
    private String city;
    @NotNull
    private String State;
    @NotNull
    private String email;
    @NotNull
    private String phone1;
    @NotNull
    private String phone2;
    @NotNull
    private String affiliation;
    @NotNull
    private String type;
    @NotNull
    private String studentRatio;
    @NotNull
    private int totalStrength;
    @NotNull
    private int numberOfOnboardedStudents;
    @NotNull
    private int numberOfRemainingStudents;
    @NotNull
    private int compensation;

}