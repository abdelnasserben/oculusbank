package com.dabel.oculusbank.service;

import com.dabel.oculusbank.dto.PaymentDTO;
import com.dabel.oculusbank.exception.TransactionNotFoundException;
import com.dabel.oculusbank.mapper.PaymentMapper;
import com.dabel.oculusbank.model.Payment;
import com.dabel.oculusbank.model.PaymentView;
import com.dabel.oculusbank.repository.PaymentRepository;
import com.dabel.oculusbank.repository.PaymentViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    PaymentViewRepository paymentViewRepository;

    public PaymentDTO save(PaymentDTO paymentDTO) {
        Payment payment = paymentRepository.save(PaymentMapper.toEntity(paymentDTO));
        return PaymentMapper.toDTO(payment);
    }

    public List<PaymentDTO> findAll() {
        return paymentViewRepository.findAll().stream()
                .map(PaymentMapper::viewToDTO)
                .collect(Collectors.toList());
    }

    public PaymentDTO findById(int paymentId) {
        PaymentView payment = paymentViewRepository.findById(paymentId)
                .orElseThrow(() -> new TransactionNotFoundException("Payment not found"));
        return PaymentMapper.viewToDTO(payment);
    }

    public List<PaymentDTO> findAllByAccountId(int accountId) {
        return paymentViewRepository.findAllByDebitAccountIdOrCreditAccountId(accountId, accountId).stream()
                .map(PaymentMapper::viewToDTO)
                .collect(Collectors.toList());
    }
}
