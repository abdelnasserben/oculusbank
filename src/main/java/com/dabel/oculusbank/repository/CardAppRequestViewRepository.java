package com.dabel.oculusbank.repository;

import com.dabel.oculusbank.model.CardAppRequestView;

import java.util.List;
import java.util.Optional;

public interface CardAppRequestViewRepository extends ReadOnlyRepository<CardAppRequestView, Integer> {

    List<CardAppRequestView> findAll();

    Optional<CardAppRequestView> findById(int loanId);
}
