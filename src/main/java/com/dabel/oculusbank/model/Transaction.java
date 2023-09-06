package com.dabel.oculusbank.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.generator.EventType;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int transactionId;
    private String transactionType;
    private int accountId;
    private double amount;
    private String currency;
    private String sourceType;
    private String sourceValue;
    private String reason;
    private String failureReason;
    private String status;
    private String initiatedBy;
    private String updatedBy;
    @CurrentTimestamp(event = EventType.INSERT)
    private LocalDateTime createdAt, updatedAt;
}
