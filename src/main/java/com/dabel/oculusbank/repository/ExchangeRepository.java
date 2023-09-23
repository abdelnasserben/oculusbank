package com.dabel.oculusbank.repository;

import com.dabel.oculusbank.model.Exchange;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExchangeRepository extends JpaRepository<Exchange, Integer> {
    List<Exchange> findAllByCustomerIdentity(String customerIdentity);
}
