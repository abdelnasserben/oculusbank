package com.dabel.oculusbank.repository;

import com.dabel.oculusbank.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan, Integer> {

}
