package com.dabel.oculusbank.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
public class AccountDTO {

    private int accountId;
    private String accountName;
    private String accountNumber;
    private String accountType;
    private String accountProfile;
    private double balance;
    private String currency;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
