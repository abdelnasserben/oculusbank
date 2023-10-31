package com.dabel.oculusbank.service.support.exchange;

import com.dabel.oculusbank.dto.ExchangeDTO;
import com.dabel.oculusbank.exception.TransactionNotFoundException;
import com.dabel.oculusbank.mapper.ExchangeMapper;
import com.dabel.oculusbank.model.Exchange;
import com.dabel.oculusbank.repository.ExchangeRepository;
import com.dabel.oculusbank.service.core.exchange.ExchangeCruService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExchangeCruServiceImpl implements ExchangeCruService {

    @Autowired
    ExchangeRepository exchangeRepository;

    @Override
    public ExchangeDTO save(ExchangeDTO exchangeDTO) {

        Exchange exchange = exchangeRepository.save(ExchangeMapper.toEntity(exchangeDTO));
        return ExchangeMapper.toDTO(exchange);
    }

    @Override
    public List<ExchangeDTO> findAll() {

        return exchangeRepository.findAll().stream()
                .map(ExchangeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ExchangeDTO findById(int exchangeId) {

        Exchange exchange = exchangeRepository.findById(exchangeId)
                .orElseThrow(() -> new TransactionNotFoundException("Exchange not found"));

        return ExchangeMapper.toDTO(exchange);
    }

    @Override
    public List<ExchangeDTO> findAllByCustomerIdentity(String customerIdentity) {

        return exchangeRepository.findAllByCustomerIdentity(customerIdentity).stream()
                .map(ExchangeMapper::toDTO)
                .collect(Collectors.toList());
    }
}
