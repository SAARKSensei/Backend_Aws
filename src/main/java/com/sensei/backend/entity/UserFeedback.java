package com.sensei.backend.entity;


import javax.persistence.*;
import javax.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "user_feedback")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Activity ID is required")
    @Column(name = "activity_id", nullable = false)
    private String activityId;

    @NotBlank(message = "Activity Name is required")
    @Column(name = "activity_name", nullable = false)
    private String activityName;

    @NotBlank(message = "Rating is required")
    @Pattern(regexp = "^[1-5]$", message = "Rating must be a number between 1 and 5")
    @Column(name = "rating", nullable = false)
    private String rating;

    @NotBlank(message = "Message cannot be empty")
    @Size(max = 500, message = "Message cannot exceed 500 characters")
    @Column(name = "message", columnDefinition = "TEXT", nullable = false)
    private String message;

    @NotBlank(message = "Parent Name is required")
    @Column(name = "parent_name", nullable = false)
    private String parentName;

    @NotBlank(message = "Child Name is required")
    @Column(name = "child_name", nullable = false)
    private String childName;

    @Email(message = "Invalid email format")
    @Column(name = "email_id")
    private String emailId;
}
