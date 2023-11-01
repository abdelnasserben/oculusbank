package com.dabel.oculusbank.repository;

import com.dabel.oculusbank.model.CardApplicationView;

import java.util.List;
import java.util.Optional;

public interface CardAppApplicationViewRepository extends ReadOnlyRepository<CardApplicationView, Integer> {

    List<CardApplicationView> findAll();

    Optional<CardApplicationView> findById(int loanId);
}
