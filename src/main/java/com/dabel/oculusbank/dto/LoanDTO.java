package com.dabel.oculusbank.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
public class LoanDTO {

    private int loanId;
    private String loanType;
    private int accountId;
    private String currency;
    private double issuedAmount;
    private double interestRate;
    private int duration;
    private double totalAmount;
    private String reason;
    private String failureReason;
    private String status;
    private String initiatedBy;
    private String updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int borrowerId;
    private int customerId;
    private String firstName;
    private String lastName;
    private String identityNumber;
}
