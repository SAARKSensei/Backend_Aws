package com.sensei.backend.application.dto;

import lombok.Data;

@Data
public class SchoolDTO {
    private String schoolId;
    private String name;
    private String addressLane1;
    private String addressLane2;
    private String city;
    private String state;
    private String email;
    private String phone1;
    private String phone2;
    private String affiliation;
    private String type;
    private String studentRatio;
    private int totalStrength;
    private int numberOfOnboardedStudents;
    private int numberOfRemainingStudents;
    private int compensation;
}
