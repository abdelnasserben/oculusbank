package com.dabel.oculusbank.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
public class CardAppRequestDTO {

    private int requestId;
    private int accountId;
    private String cardType;
    private String status;
    private String failure_reason;
    private String initiatedBy;
    private String updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String accountName;
    private String accountNumber;
}
