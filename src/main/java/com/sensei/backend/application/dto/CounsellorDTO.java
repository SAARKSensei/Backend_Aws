package com.sensei.backend.application.dto;

import lombok.Data;
import java.util.Date;

@Data
public class CounsellorDTO {
    private String counsellorId;
    private String name;
    private String email;
    private String phone;
    private Date dateOfBirth;
    private String address;
    private String city;
    private String state;
    private String country;
    private Double pincode;
    private String hscSchoolName;
    private int hscGrade;
    private Date hscYearOfPassing;
    private String sscSchoolName;
    private int sscGrade;
    private Date sscYearOfPassing;
    private String ugCollegeName;
    private int ugCollegeGrade;
    private Date ugYearOfPassing;
    private String pgCollegeName;
    private int pgCollegeGrade;
    private Date pgYearOfPassing;
    private int yearOfWorkExperience;
    private String otherCertifications;
    private boolean willingToWorkFullTime;
    private boolean willingToTakeOneOnOneSessions;
    private int perHourCharges;
}
