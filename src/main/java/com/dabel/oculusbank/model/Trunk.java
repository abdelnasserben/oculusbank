package com.dabel.oculusbank.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "trunks")
public class Trunk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int trunkId;
    private int accountId;
    private int customerId;
    private String membership;
}
