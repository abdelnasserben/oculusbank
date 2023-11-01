package com.dabel.oculusbank.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@Entity
@Table(name = "borrowers")
public class Borrower {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int borrowerId;
    private int loanId;
    private int customerId;
}
