package com.dabel.oculusbank.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Immutable;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "v_cards")
@Immutable
public class CardView {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cardId;
    private int accountId;
    private String cardType;
    private String cardNumber;
    private String cardName;
    private LocalDate expirationDate;
    private String cvc;
    private int cvcChecked;
    private String status;
    private String initiatedBy;
    private String updatedBy;
    private String failureReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String accountName;
    private String accountNumber;
}
