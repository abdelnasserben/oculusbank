package com.dabel.oculusbank.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.generator.EventType;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "branchs")
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int branchId;
    private String branchName;
    private String branchAddress;
    private String status;
    @CurrentTimestamp(event = EventType.INSERT)
    private LocalDateTime createdAt, updatedAt;
}
