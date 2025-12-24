package com.sensei.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionOption {

    // @Id
    // @GeneratedValue(generator = "system-uuid")
    // @GenericGenerator(name="system-uuid", strategy = "uuid")
    // private String id;
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    private String optionText;
    private String status;
    private String counsellorNote;
}
