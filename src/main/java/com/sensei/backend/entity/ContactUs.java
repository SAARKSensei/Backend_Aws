package com.sensei.backend.entity;

import java.util.UUID;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactUs {
    // @Id
    // @GeneratedValue(generator = "system-uuid")
    // @GenericGenerator(name="system-uuid",strategy = "uuid")
    // private Long userId;
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;


    @NotBlank(message = "Please Add User Name")
    private String userName;
    private String Email;
    private String phoneNumber;
    private String message;
}
