package com.dabel.oculusbank.repository;

import com.dabel.oculusbank.model.Exchange;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeRepository extends JpaRepository<Exchange, Integer> {
}
