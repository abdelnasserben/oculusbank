package com.dabel.oculusbank.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Immutable;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "v_card_applications")
@Immutable
public class CardAppRequestView {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private String customerFirstName;
    private String customerLastName;
    private String customerIdentityNumber;
}
