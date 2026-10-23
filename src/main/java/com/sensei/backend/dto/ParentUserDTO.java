package com.sensei.backend.dto;

import lombok.Data;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonInclude;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
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

    @NotBlank(message = "Child lock PIN is required")
    @Pattern(regexp = "^[0-9]{4}$", message = "Child lock PIN must be 4 digits")
    @com.fasterxml.jackson.annotation.JsonProperty(access = com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY)
    private String childLock;

    private Boolean isChildLockSet;

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

    private String pincode;
    private String street;
    private String city;
    private String state;
    private String country;
}
