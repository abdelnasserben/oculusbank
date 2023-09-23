package com.dabel.oculusbank.repository;

import com.dabel.oculusbank.model.TransactionView;

import java.util.List;
import java.util.Optional;

public interface TransactionViewRepository extends ReadOnlyRepository<TransactionView, Integer> {
    List<TransactionView> findAll();
    Optional<TransactionView> findById(int transactionId);
    List<TransactionView> findAllByAccountId(int accountId);
}
