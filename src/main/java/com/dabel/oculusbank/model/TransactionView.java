package com.dabel.oculusbank.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Immutable;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "v_transactions")
@Immutable
public class TransactionView {

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
    private String customerIdentity;
    private String customerFullName;
    private String failureReason;
    private String status;
    private String initiatedBy;
    private String updatedBy;
    private LocalDateTime createdAt, updatedAt;
    private String accountName;
    private String accountNumber;
}
