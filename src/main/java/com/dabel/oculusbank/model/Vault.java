package com.dabel.oculusbank.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "vaults")
public class Vault {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int vaultId;
    private int accountId;
    private int branchId;
}
