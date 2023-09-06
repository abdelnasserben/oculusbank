package com.dabel.oculusbank.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
public class TransactionDTO {

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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String initiatedBy;
    private String updatedBy;
    private String accountName;
    private String accountNumber;
}
