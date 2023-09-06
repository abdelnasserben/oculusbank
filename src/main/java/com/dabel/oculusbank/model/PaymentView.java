package com.dabel.oculusbank.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Immutable;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "v_payments")
@Immutable
public class PaymentView {

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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String initiatedBy;
    private String updatedBy;
    private String debitAccountName;
    private String debitAccountNumber;
    private String creditAccountName;
    private String creditAccountNumber;
}
