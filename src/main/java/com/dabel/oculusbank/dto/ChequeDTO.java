package com.dabel.oculusbank.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
public class ChequeDTO {

    private int chequeId;
    private int accountId;
    private String chequeName;
    private String chequeNumber;
    private String status;
    private String initiatedBy;
    private String updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String accountName;
    private String accountNumber;
}
