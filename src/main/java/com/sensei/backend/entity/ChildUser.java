// package com.sensei.backend.entity;

// import com.sun.istack.NotNull;
// import lombok.AllArgsConstructor;
// import lombok.Builder;
// import lombok.Data;
// import lombok.NoArgsConstructor;
// import org.hibernate.annotations.GenericGenerator;

// import javax.persistence.*;
// import java.util.Date;

// @Entity
// @Data
// @NoArgsConstructor
// @AllArgsConstructor
// @Builder
// // @Table(name = "ChildUser") // Added table ChildUser by Vaishnav Kale
// public class ChildUser {

//     @Id
//     @GeneratedValue(generator = "system-uuid")
//     @GenericGenerator(name = "system-uuid", strategy = "uuid")
//     private String childId;

//     //@NotNull
//     private String childName;

//     //@NotNull
//     private String schoolId;

//     //@NotNull
//     private String grade;

//     //@NotNull
//     @Temporal(TemporalType.DATE)
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

//     private String parentNumber;

//     //@NotNull
//     private long phoneNumber;

//     @OneToOne
//     @JoinColumn(name = "parent_id", referencedColumnName = "parentId")
//     private ParentUser parentUser;  // Establishing OneToOne relationship with ParentUser

//     // New Field: Plan Start Date
//     @Temporal(TemporalType.DATE)
//     private Date planStartDate;

//     @Temporal(TemporalType.DATE)
//     private Date planExpiryDate;
// }
package com.sensei.backend.entity;

import lombok.*;
import javax.persistence.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

import com.sensei.backend.enums.PlanStatus;

@Entity
@Table(name = "child_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChildUser {

    @Id
    @GeneratedValue
    @Column(
        name = "child_id",
        columnDefinition = "uuid",
        updatable = false,
        nullable = false
    )
    private UUID childId;

    @Column(name = "child_name")
    private String childName;

    private String gender;
    private String grade;

    @Column(name = "age_group")
    private String ageGroup;

    @Column(name = "blood_group")
    private String bloodGroup;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    // 🔗 Parent
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", nullable = false)
    private ParentUser parentUser;

    // 🔐 Active plan reference
    @Column(name = "active_plan_id")
    private UUID activePlanId;

    @Column(name = "plan_start_date")
    private LocalDate planStartDate;

    @Column(name = "plan_expiry_date")
    private LocalDate planExpiryDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "plan_status")
    private PlanStatus planStatus;
}
