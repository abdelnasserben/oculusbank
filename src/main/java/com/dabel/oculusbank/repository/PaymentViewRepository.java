package com.dabel.oculusbank.repository;

import com.dabel.oculusbank.model.PaymentView;

import java.util.List;
import java.util.Optional;

public interface PaymentViewRepository extends ReadOnlyRepository<PaymentView, Integer> {
    List<PaymentView> findAll();
    Optional<PaymentView> findById(int paymentId);
}
