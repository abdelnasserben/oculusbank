package com.dabel.oculusbank.repository;

import com.dabel.oculusbank.model.VaultView;

import java.util.List;
import java.util.Optional;

public interface VaultViewRepository extends ReadOnlyRepository<VaultView, Integer> {
    List<VaultView> findAllByBranchId(int branchId);
    Optional<VaultView> findByBranchId(int branchId);
}
