package com.dabel.oculusbank.repository;

import com.dabel.oculusbank.model.Vault;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VaultRepository extends JpaRepository<Vault, Integer> {
}
