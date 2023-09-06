package com.dabel.oculusbank.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
public class PaymentDTO {

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
