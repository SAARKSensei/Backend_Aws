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
public class Module {
    // @Id
    // @GeneratedValue(generator = "system-uuid")
    // @GenericGenerator(name="system-uuid",strategy = "uuid")
    // private String moduleId;
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @NotBlank
    private String moduleName;

    private int moduleNumber;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "module_id")
    private List<SubModule> subModules;

    // New Column
    @Column(columnDefinition = "TEXT")
    private String subjectIdRef;

    // New Column
//    @Column(columnDefinition = "TEXT")
//    private String submoduleIdRef;

    @ElementCollection
    @Column(columnDefinition = "TEXT")
    private List<String> submoduleIdRef; // Store multiple submodule IDs
}