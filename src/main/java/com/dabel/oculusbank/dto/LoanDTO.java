package com.dabel.oculusbank.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
public class LoanDTO {

    private int loanId;
    @NotBlank
    private String loanType;
    private int accountId;
    private String currency;
    @Positive
    private double issuedAmount;
    @PositiveOrZero
    private double interestRate;
    @Positive
    private int duration;
    private double totalAmount;
    private double perMonthAmount;
    private double remainingAmount;
    @NotBlank
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
    @NotBlank
    private String identityNumber;
    private String accountName;
    private String accountNumber;
}
