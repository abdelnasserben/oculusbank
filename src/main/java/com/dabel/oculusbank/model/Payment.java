package com.dabel.oculusbank.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.generator.EventType;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int paymentId;
    private int debitAccountId;
    private int creditAccountId;
    private double amount;
    private String currency;
    private String reason;
    private String failureReason;
    private String status;
    private String initiatedBy;
    private String updatedBy;
    @CurrentTimestamp(event = EventType.INSERT)
    private LocalDateTime createdAt;
    @CurrentTimestamp(event = {EventType.INSERT, EventType.UPDATE})
    private LocalDateTime updatedAt;
}
