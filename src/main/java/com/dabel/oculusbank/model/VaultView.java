package com.dabel.oculusbank.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Immutable;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "v_vaults")
@Immutable
public class VaultView {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int accountId;
    private String accountName;
    private String accountNumber;
    private String accountType;
    private String accountProfile;
    private double balance;
    private String currency;
    private String status;
    private LocalDateTime createdAt, updatedAt;

    private int vaultId;
    private int branchId;
}
