package com.dabel.oculusbank.repository;

import com.dabel.oculusbank.model.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BranchRepository extends JpaRepository<Branch, Integer> {
}
