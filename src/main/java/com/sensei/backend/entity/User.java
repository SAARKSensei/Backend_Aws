package com.sensei.backend.entity;

import java.util.UUID;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
	// @Id
    // @GeneratedValue(generator = "system-uuid") // ✅ Auto-generate UUID
    // @GenericGenerator(name = "system-uuid", strategy = "uuid") // ✅ Hibernate UUID strategy
    // @Column(name = "user_id", unique = true, nullable = false)
    // private String userId; // using String to store UUID as text
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    private String googleId;
    private String name;
    private String email;
    private String profilePicture;
}
