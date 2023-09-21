package com.dabel.oculusbank.dto;

import com.dabel.oculusbank.app.validation.CardType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.CreditCardNumber;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
public class CardDTO {

    private int cardId;
    private int accountId;
    @CardType
    private String cardType;
    @CreditCardNumber
    private String cardNumber;
    @NotBlank
    private String cardName;
    private LocalDate expirationDate;
    @Size(min = 3, max = 4)
    private String cvc;
    private int cvcChecked;
    private String status;
    private String initiatedBy;
    private String updatedBy;
    private String failureReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String accountName;
    @NotBlank
    private String accountNumber;
}
