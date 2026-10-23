package com.sensei.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sensei.backend.enums.PlanStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

/**
 * DTO for partial updates to ChildUser.
 * All fields are optional; non-null values will update the entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChildUserUpdateDTO {

    private String childName;
    private String gender;
    private String grade;
    private String ageGroup;
    private String bloodGroup;
    private String phoneNumber;
    private Date dateOfBirth;
    private String schoolName;
    private String schoolPhone;
    private String schoolEmail;
    private String schoolAddress;
    private String medicalHistory;
    private String pfp;
    private UUID parentId;
    private UUID activePlanId;
    private LocalDate planStartDate;
    private LocalDate planExpiryDate;
    private PlanStatus planStatus;
}
