package com.dabel.oculusbank.repository;

import com.dabel.oculusbank.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Optional<Customer> findByIdentityNumber(String identityNumber);
}
