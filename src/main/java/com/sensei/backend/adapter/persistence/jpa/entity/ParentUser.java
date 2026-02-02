// package com.sensei.backend.entity;

// import lombok.AllArgsConstructor;
// import lombok.Builder;
// import lombok.Data;
// import lombok.NoArgsConstructor;
// import org.hibernate.annotations.GenericGenerator;

// import javax.persistence.*;
// import java.util.Date;
// import java.util.List;



// @Entity
// @Data
// @NoArgsConstructor
// @AllArgsConstructor
// @Builder
// // @Table(name="ParentUser",uniqueConstraints = {@UniqueConstraint(columnNames = {"userName"})})  // Added name="ParentUser" by Vaishnav Kale
// public class ParentUser {

//     @Id
//     @GeneratedValue(generator = "system-uuid")
//     @GenericGenerator(name = "system-uuid", strategy = "uuid")
//     private String parentId;

//     private String name;

//     private String userName;

//     private String email;

//     private String phone;

//     private String password;

//     private String maritalStatus;

//     private String occupation;

//     @Temporal(TemporalType.DATE)
//     private Date dateOfBirth;

//     private String relationWithChildren;

//     private String spouseName;

//     private String spouseGender;

//     private String spouseEmail;

//     private String spousePhone;

//     private String spouseOccupation;

//     @Temporal(TemporalType.DATE)
//     private Date spouseDateOfBirth;

//     private String spouseRelationWithChild;

//     private String childNameRef;

//     @OneToMany(mappedBy = "parentUser", cascade = CascadeType.ALL, orphanRemoval = true)       // Modified OneToMany by Vaishnav Kale
//     private List<ChildUser> childUsers;  // Bidirectional OneToOne relationship         // made list by Vaishnav Kale

//     public String getParentId() {
//         return parentId;
//     }   // Added by Vaishnav Kale
// }
package com.sensei.backend.adapter.persistence.jpa.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "parent_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParentUser {

    @Id
    @GeneratedValue
    @Column(name = "parent_id", columnDefinition = "uuid")
    private UUID parentId;

    private String name;

    @Column(name = "user_name", unique = true)
    private String userName;

    @Column(unique = true)
    private String email;

    private String phone;
    private String password;
    private String maritalStatus;
    private String occupation;
    private LocalDate dateOfBirth;

    private String relationWithChildren;

    // Spouse Info
    private String spouseName;
    private String spouseGender;
    private String spouseEmail;
    private String spousePhone;
    private String spouseOccupation;
    private LocalDate spouseDateOfBirth;
    private String spouseRelationWithChild;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // 🔗 FIXED mappedBy
    @OneToMany(mappedBy = "parentUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChildUser> childUsers = new ArrayList<>();
}

