package com.dabel.oculusbank.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "v_borrowings")
public class LoanView {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
