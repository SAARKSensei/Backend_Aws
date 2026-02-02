// package com.sensei.backend.dto;

// import lombok.Data;

// import javax.validation.constraints.NotNull;
// import java.util.Date;

// @Data
// public class ChildUserDTO {
//     private String childId;

//     private String parentId;

//     //@NotNull
//     private String childName;

//     //@NotNull
//     private String schoolId;

//     //@NotNull
//     private String grade;

//     //@NotNull
//     private Date dateOfBirth;

//     //@NotNull
//     private boolean visitingCounsellor;

//     //@NotNull
//     private boolean anyMedicalHistory;

//     private String medicalHistoryDescription;

//     //@NotNull
//     private String bloodGroup;

//     //@NotNull
//     private String ageGroup;

//     //@NotNull
//     private String activePlanId;

//     private long phoneNumber;

//     // New Field: Plan Start Date
//     private Date planStartDate;

//     private Date planExpiryDate;
// }
package com.sensei.backend.application.dto;

import lombok.Data;
import java.util.Date;
import java.util.UUID;

@Data
public class ChildUserDTO {

    private UUID childId;
    private String childName;
    private String gender;
    private String grade;
    private String ageGroup;
    private String bloodGroup;
    private String phoneNumber;
    private Date dateOfBirth;

    private UUID parentId;
    private UUID activePlanId;
    private Date planStartDate;
}

