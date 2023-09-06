package com.dabel.oculusbank.repository;

import com.dabel.oculusbank.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
}
