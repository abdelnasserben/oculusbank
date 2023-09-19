package com.dabel.oculusbank.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.generator.EventType;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int accountId;
    private String accountName;
    private String accountNumber;
    private String accountType;
    private String accountProfile;
    private double balance;
    private String currency;
    private String status;
    @CurrentTimestamp(event = EventType.INSERT)
    private LocalDateTime createdAt;
    @CurrentTimestamp(event = {EventType.INSERT, EventType.UPDATE})
    private LocalDateTime updatedAt;
}
