package com.dabel.oculusbank.mapper;

import com.dabel.oculusbank.dto.PaymentDTO;
import com.dabel.oculusbank.model.Payment;
import com.dabel.oculusbank.model.PaymentView;
import org.modelmapper.ModelMapper;

public class PaymentMapper {

    private static final ModelMapper mapper = new ModelMapper();

    public static Payment toEntity(PaymentDTO paymentDTO) {
        return mapper.map(paymentDTO, Payment.class);
    }

    public static PaymentDTO toDTO(Payment payment) {
        return mapper.map(payment, PaymentDTO.class);
    }

    public static PaymentDTO viewToDTO(PaymentView paymentView) {
        return mapper.map(paymentView, PaymentDTO.class);
    }
}
