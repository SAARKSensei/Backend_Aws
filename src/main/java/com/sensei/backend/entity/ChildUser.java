package com.sensei.backend.entity;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// @Table(name = "ChildUser") // Added table ChildUser by Vaishnav Kale
public class ChildUser {

    // @Id
    // @GeneratedValue(generator = "system-uuid")
    // @GenericGenerator(name = "system-uuid", strategy = "uuid")
    // private String childId;
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    //@NotNull
    private String childName;

    //@NotNull
    private String schoolId;

    //@NotNull
    private String grade;

    //@NotNull
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    //@NotNull
    private boolean visitingCounsellor;

    //@NotNull
    private boolean anyMedicalHistory;

    private String medicalHistoryDescription;

    //@NotNull
    private String bloodGroup;

    //@NotNull
    private String ageGroup;

    //@NotNull
    private String activePlanId;

    private String parentNumber;

    //@NotNull
    private long phoneNumber;

    @OneToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "parentId")
    private ParentUser parentUser;  // Establishing OneToOne relationship with ParentUser

    // New Field: Plan Start Date
    @Temporal(TemporalType.DATE)
    private Date planStartDate;

    @Temporal(TemporalType.DATE)
    private Date planExpiryDate;
}
