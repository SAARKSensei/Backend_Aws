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
public class SubModule {
    // @Id
    // @GeneratedValue(generator = "system-uuid")
    // @GenericGenerator(name="system-uuid",strategy = "uuid")
    // private String subModuleId;
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;
    @NotBlank
    private String subModuleName;

    private int subModuleNumber;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "sub_module_id")
    private List<InteractiveActivity> interactiveActivities;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "sub_module_id")
    private List<DigitalActivity> digitalActivities;

    // New Column
    @Column(columnDefinition = "TEXT")
    private String moduleIdRef;

    // New Column
    @Column(columnDefinition = "TEXT")
    private String interactiveActivityIdRef;

    // New Column
    @Column(columnDefinition = "TEXT")
    private String digitalActivityIdRef;
}