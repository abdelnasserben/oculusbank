package com.dabel.oculusbank.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.generator.EventType;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "cheques")
public class Cheque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int chequeId;
    private int accountId;
    private String chequeName;
    private String chequeNumber;
    private String status;
    private String initiatedBy;
    private String updatedBy;
    @CurrentTimestamp(event = EventType.INSERT)
    private LocalDateTime createdAt;
    @CurrentTimestamp(event = {EventType.INSERT, EventType.UPDATE})
    private LocalDateTime updatedAt;
}
