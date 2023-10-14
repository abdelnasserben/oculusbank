package com.dabel.oculusbank.service.core.payment;

import com.dabel.oculusbank.dto.PaymentDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PaymentCrudService {

    PaymentDTO save(PaymentDTO paymentDTO);

    List<PaymentDTO> findAll();

    PaymentDTO findById(int paymentId);

    List<PaymentDTO> findAllByAccountId(int accountId);
}
