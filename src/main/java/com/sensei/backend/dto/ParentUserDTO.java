package com.sensei.backend.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;


@Data
public class ParentUserDTO {
    private String parentId;

    //@NotNull
    private String name;

    //@NotNull
    private String userName;

    //@NotNull
    private String email;

    //@NotNull
    private String phone;

    //@NotNull
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

    private List<ChildUserDTO> childUsers;        // Added by Vaishnav Kale
}
