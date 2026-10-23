package com.sensei.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * DTO for partial updates to ParentUser.
 * All fields are optional; non-null values will update the entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ParentUserUpdateDTO {

    private String name;
    private String userName;

    @Email(message = "Invalid email format")
    private String email;

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
    private String pincode;
    private String street;
    private String city;
    private String state;
    private String country;
    private Boolean isQuizCompleted;
}
