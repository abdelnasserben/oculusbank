package com.dabel.oculusbank.dto;

import com.dabel.oculusbank.app.validation.CardType;
import jakarta.validation.constraints.NotBlank;
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
    private int customerId;
    @CardType
    private String cardType;
    private String status;
    private String failure_reason;
    private String initiatedBy;
    private String updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String accountName;
    @NotBlank
    private String accountNumber;
    private String customerFirstName;
    private String customerLastName;
    @NotBlank
    private String customerIdentityNumber;
}
