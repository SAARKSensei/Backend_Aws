package com.sensei.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Subject {
    // @Id
    // @GeneratedValue(generator = "system-uuid")
    // @GenericGenerator(name="system-uuid",strategy = "uuid")
    // private String subjectId;
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @NotBlank
    private String subjectName;

    @NotBlank
    private String ageGroup;

    private int duration;

    private int progress;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "subject_id")
    private List<Module> modules;

    // New Column
    @Column(columnDefinition = "TEXT")
    private String moduleIdRef;

    public String getName() {
        return subjectName;
    }  // Added by Vaishnav Kale
}