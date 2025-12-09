package com.sensei.backend.entity;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "lifeskills")
public class LifeSkill {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "lifeskill_id", updatable = false, nullable = false)
    private String lifeskillId = UUID.randomUUID().toString();

    @Column(name = "lifeskill_name", nullable = false)
    private String lifeskillName;

    // Getters and setters
    public String getLifeskillId() { return lifeskillId; }
    public void setLifeskillId(UUID lifeskillId) { this.lifeskillId = UUID.randomUUID().toString(); }

    public String getLifeskillName() { return lifeskillName; }
    public void setLifeskillName(String lifeskillName) { this.lifeskillName = lifeskillName; }
}
