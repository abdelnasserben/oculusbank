package com.dabel.oculusbank.repository;

import com.dabel.oculusbank.model.Cheque;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChequeRepository extends JpaRepository<Cheque, Integer> {

}
