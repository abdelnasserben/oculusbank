package com.dabel.oculusbank.repository;

import com.dabel.oculusbank.model.LoanView;

import java.util.List;
import java.util.Optional;

public interface LoanViewRepository extends ReadOnlyRepository<LoanView, Integer> {
    List<LoanView> findAll();
    Optional<LoanView> findById(int loanId);
    List<LoanView> findAllByIdentityNumber(String customerIdentityNumber);
}
