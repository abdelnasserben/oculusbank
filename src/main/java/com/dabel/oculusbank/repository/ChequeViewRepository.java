package com.dabel.oculusbank.repository;

import com.dabel.oculusbank.model.ChequeView;

import java.util.List;
import java.util.Optional;

public interface ChequeViewRepository extends ReadOnlyRepository<ChequeView, Integer> {
    Optional<ChequeView> findByChequeNumber(String chequeNumber);
}
