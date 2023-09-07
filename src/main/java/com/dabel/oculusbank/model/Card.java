package com.dabel.oculusbank.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.generator.EventType;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "cards")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @CurrentTimestamp(event = EventType.INSERT)
    private LocalDateTime createdAt, updatedAt;
}
