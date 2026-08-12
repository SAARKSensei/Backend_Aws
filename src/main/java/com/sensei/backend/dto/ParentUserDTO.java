// package com.sensei.backend.dto;

// import lombok.Data;

// import jakarta.validation.constraints.NotNull;
// import java.util.Date;
// import java.util.List;


// @Data
// public class ParentUserDTO {
//     private String parentId;

//     //@NotNull
//     private String name;

//     //@NotNull
//     private String userName;

//     //@NotNull
//     private String email;

//     //@NotNull
//     private String phone;

//     //@NotNull
//     private String password;

//     private String maritalStatus;

//     private String occupation;

//     private Date dateOfBirth;

//     private String relationWithChildren;

//     private String spouseName;

//     private String spouseGender;

//     private String spouseEmail;

//     private String spousePhone;

//     private String spouseOccupation;

//     private Date spouseDateOfBirth;

//     private String spouseRelationWithChild;

//     private List<ChildUserDTO> childUsers;        // Added by Vaishnav Kale
// }
package com.sensei.backend.dto;

import lombok.Data;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import jakarta.validation.constraints.*;

@Data
public class ParentUserDTO {

    private UUID parentId;   // was String

    @NotBlank(message = "Name is required")
    private String name;

    private String userName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone must be 10 digits")
    private String phone;

    private String password;
    private String maritalStatus;
    private String occupation;
    private Date dateOfBirth;
    private String relationWithChildren;
    private String spouseName;
    private String spouseGender;
    private String spouseEmail;
    private String spousePhone;
    private String spouseOccupation;
    private Date spouseDateOfBirth;
    private String spouseRelationWithChild;

    private List<ChildUserDTO> childUsers;

    // ✅ NEW FIELD
    @NotBlank(message = "Location is required")
    private String location;
}

