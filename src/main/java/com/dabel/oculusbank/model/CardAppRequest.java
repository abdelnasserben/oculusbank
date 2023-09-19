package com.dabel.oculusbank.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.generator.EventType;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "card_applications")
public class CardAppRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int requestId;
    private int accountId;
    private String cardType;
    private String status;
    private String failure_reason;
    private String initiatedBy;
    private String updatedBy;
    @CurrentTimestamp(event = EventType.INSERT)
    private LocalDateTime createdAt;
    @CurrentTimestamp(event = {EventType.INSERT, EventType.UPDATE})
    private LocalDateTime updatedAt;
}
