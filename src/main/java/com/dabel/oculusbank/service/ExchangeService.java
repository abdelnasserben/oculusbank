package com.dabel.oculusbank.service;

import com.dabel.oculusbank.constant.Status;
import com.dabel.oculusbank.dto.ExchangeDTO;
import com.dabel.oculusbank.exception.TransactionNotFoundException;
import com.dabel.oculusbank.mapper.ExchangeMapper;
import com.dabel.oculusbank.model.Exchange;
import com.dabel.oculusbank.repository.ExchangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExchangeService {

    @Autowired
    ExchangeRepository exchangeRepository;

    public ExchangeDTO save(ExchangeDTO exchangeDTO) {
        Exchange exchange = exchangeRepository.save(ExchangeMapper.toEntity(exchangeDTO));
        return ExchangeMapper.toDTO(exchange);
    }

    public List<ExchangeDTO> findAll() {
        return exchangeRepository.findAll().stream()
                .map(ExchangeService::formatStatusToNameAndGetDTO)
                .collect(Collectors.toList());
    }

    public ExchangeDTO findById(int exchangeId) {
        Exchange exchange = exchangeRepository.findById(exchangeId)
                .orElseThrow(() -> new TransactionNotFoundException("Exchange not found"));

        return formatStatusToNameAndGetDTO(exchange);
    }

    private static ExchangeDTO formatStatusToNameAndGetDTO(Exchange exchange) {
        exchange.setStatus(Status.nameOf(exchange.getStatus()));
        return ExchangeMapper.toDTO(exchange);
    }
}
