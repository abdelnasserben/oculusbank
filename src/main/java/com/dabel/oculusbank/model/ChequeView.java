package com.dabel.oculusbank.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.annotations.Immutable;
import org.hibernate.generator.EventType;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "v_cheques")
@Immutable
public class ChequeView {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int chequeId;
    private int accountId;
    private String chequeName;
    private String chequeNumber;
    private String status;
    private String initiatedBy;
    private String updatedBy;
    private LocalDateTime createdAt, updatedAt;
    private String accountName;
    private String accountNumber;
}
