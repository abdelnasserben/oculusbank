package com.dabel.oculusbank.repository;

import com.dabel.oculusbank.model.Borrower;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowerRepository extends JpaRepository<Borrower, Integer> {
    
}
