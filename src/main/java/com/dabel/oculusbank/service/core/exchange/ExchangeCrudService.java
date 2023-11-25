package com.dabel.oculusbank.service.core.exchange;

import com.dabel.oculusbank.dto.ExchangeDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ExchangeCrudService {

    ExchangeDTO save(ExchangeDTO exchangeDTO);

    List<ExchangeDTO> findAll();

    ExchangeDTO findById(int exchangeId);

    List<ExchangeDTO> findAllByCustomerIdentity(String customerIdentity);
}
