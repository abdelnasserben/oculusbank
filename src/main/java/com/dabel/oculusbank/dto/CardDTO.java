package com.dabel.oculusbank.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
public class CardDTO {

    private int cardId;
    private int accountId;
    private String cardType;
    private String cardNumber;
    private String cardName;
    private LocalDate expirationDate;
    private String cvcChecked;
    private String issuer;
    private String billingAddress;
    private String status;
    private String initiatedBy;
    private String updatedBy;
    private String failureReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String accountName;
    private String accountNumber;
}
