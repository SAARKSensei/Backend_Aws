package com.sensei.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Subject {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name="system-uuid",strategy = "uuid")
    private String subjectId;

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