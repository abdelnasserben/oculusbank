package com.dabel.oculusbank.repository;

import com.dabel.oculusbank.model.CardView;

import java.util.List;
import java.util.Optional;

public interface CardViewRepository extends ReadOnlyRepository<CardView, Integer> {

    List<CardView> findAll();

    Optional<CardView> findById(int loanId);

    Optional<CardView> findByCardNumber(String cardNumber);

    List<CardView> findAllByAccountId(int accountId);
}
