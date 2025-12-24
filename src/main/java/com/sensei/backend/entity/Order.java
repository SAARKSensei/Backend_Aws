package com.sensei.backend.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.context.annotation.Primary;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="order_table")
public class Order {

    // @Id
    // private String id;
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;
    private String entity;
    private int amount;
    @JsonProperty("amount_paid")
    private int amountPaid;
    @JsonProperty("amount_due")
    private int amountDue;
    private String currency;
    private String receipt;
    @JsonProperty("offer_id")
    private String offerId;
    private String status;
    private int attempts;
    @JsonProperty("created_at")
    private Date createdAt;

}
