package com.dabel.oculusbank.repository;

import com.dabel.oculusbank.model.TrunkView;

import java.util.List;
import java.util.Optional;

public interface TrunkViewRepository extends ReadOnlyRepository<TrunkView, Integer> {

    List<TrunkView> findAllByCustomerId(int customerId);

    Optional<TrunkView> findByCustomerId(int customerId);

    Optional<TrunkView> findByAccountNumber(String accountNumber);

    Optional<TrunkView> findByAccountNumberAndCustomerIdentityNumber(String accountNumber, String customerIdentity);
}
