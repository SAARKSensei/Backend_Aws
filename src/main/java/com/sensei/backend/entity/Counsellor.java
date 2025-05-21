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
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Counsellor {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name="system-uuid",strategy = "uuid")
    private String CounsellorId;
    @NotNull
    private String name;
    @NotNull
    private String email;
    @NotNull
    private String phone;
    @NotNull
    private Date dateOfBirth;
    @NotNull
    private String address;
    @NotNull
    private String city;
    @NotNull
    private String State;
    @NotNull
    private String country;
    @NotNull
    private Double pincode;
    @NotNull
    private String hscSchoolName;
    @NotNull
    private int hscGrade;
    @NotNull
    private Date hscYearOfPassing;
    @NotNull
    private String sscSchoolName;
    @NotNull
    private int sscGrade;
    @NotNull
    private Date sscYearOfPassing;
    @NotNull
    private String ugCollegeName;
    @NotNull
    private int ugCollegeGrade;
    @NotNull
    private Date ugYearOfPassing;
    @NotNull
    private String pgCollegeName;
    @NotNull
    private int pgCollegeGrade;
    @NotNull
    private Date pgYearOfPassing;
    @NotNull
    private int yearOfWorkExperience;
    @NotNull
    private String otherCertifications;
    @NotNull
    private boolean willingToWorkFullTime;
    @NotNull
    private boolean willingToTakeOneOnOneSessions;
    @NotNull
    private int perHourCharges;


}